@file:JvmName("Benchmarker")

package xyz.gmitch215.benchmarks.measurement

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import oshi.SystemInfo
import xyz.gmitch215.benchmarks.*
import java.io.File
import java.lang.System
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.set

const val RUN_COUNT = 25
const val EMPTY_RETRY_COUNT = 10
val json = Json {
    prettyPrint = true
}
lateinit var LIBRARY_DIRECTORY: File

private fun countChildren(count: AtomicInteger, job: Job) {
    count.incrementAndGet()
    job.children.forEach { countChildren(count, it) }
}

suspend fun main(args: Array<String>): Unit = withContext(Dispatchers.IO) {
    logger.info { "Starting Benchmarks" }
    logger.debug { "Arguments: ${args.joinToString()}" }

    val input = File(args[0])
    val output = File(input, "output")
    LIBRARY_DIRECTORY = File(args[1])

    val filter = args.getOrNull(2)?.let { Regex(it) }
    if (filter != null)
        logger.info { "Filtering benchmarks with '$filter'" }

    val file = File(input, "config.yml").readText(Charsets.UTF_8)
    val languages = Yaml.default.decodeFromString<List<Language>>(file)

    logger.debug { "Found ${languages.size} Languages" }

    logger.info { "Starting Benchmarks on $os" }

    val folders = input.listFiles { f -> f?.let { it.isDirectory && it.name !in EXCLUDE_FOLDERS } == true } ?: emptyArray()
    logger.debug { "Found ${folders.size} folders" }
    logger.debug { "Folders: ${folders.joinToString { it.absolutePath}}" }

    if (folders.isEmpty())
        error("No benchmarks found")

    // Run Benchmarks

    val results = mutableMapOf<String, List<BenchmarkResult>>()

    val job = launch(Dispatchers.Default) {
        for (lang in languages) {
            val out = File(output, lang.id)
            if (!out.exists())
                out.mkdirs()

            logger.debug { "Starting language '${lang.id}'" }

            val langJob = launch {
                for (f in folders) {
                    val name = f.name
                    if (filter != null && !filter.matches(name))
                        continue

                    val config = File(f, "config.yml")
                    if (!config.exists())
                        error("Configuration file does not exist: ${config.absolutePath}")

                    logger.debug { "Starting benchmark '$name' for '${lang.id}'" }
                    val benchmarkJob = launch {
                        val outputFile = File(out, "$name.json")
                        if (outputFile.exists()) {
                            logger.info { "Skipping existing benchmark '${outputFile.absolutePath}'" }

                            val data = json.decodeFromString<BenchmarkResult>(outputFile.readText())
                            if (results.contains(name))
                                results[f.name] = results[f.name]!! + data
                            else
                                results[f.name] = listOf(data)

                            logger.debug { "Loaded existing benchmark '${name}' for '${lang.id}'" }
                            return@launch
                        }

                        val result = runBenchmark(lang, f, out).await()
                        logger.debug { "Received result for '$name' under '${lang.id}'" }

                        if (result == null) return@launch // Disabled Language

                        if (results.contains(name))
                            results[f.name] = results[f.name]!! + result
                        else
                            results[f.name] = listOf(result)

                        logger.debug { "Finished benchmark '$name' for '${lang.id}'" }
                    }

                    if (logger.isDebugEnabled())
                        launch(Dispatchers.Default) {
                            while (benchmarkJob.isActive) {
                                val count = AtomicInteger()
                                countChildren(count, benchmarkJob)

                                logger.debug { "Benchmark '$name' for language '${lang.id}' task running with $count children" }
                                delay(5000)
                            }

                            logger.debug { "Benchmark '$name' for language '${lang.id}' task finished" }
                        }
                }
            }

            if (logger.isDebugEnabled())
                launch(Dispatchers.Default) {
                    while (langJob.isActive) {
                        val count = AtomicInteger()
                        countChildren(count, langJob)

                        logger.debug { "--- Language '${lang.id}' parent task running with $count children" }
                        delay(5000)
                    }

                    logger.debug { "Language '${lang.id}' parent task finished" }
                }
        }
    }

    // Rank Benchmarks
    job.invokeOnCompletion {
        launch {
            rankBenchmarks(results, output)
        }
    }

    if (logger.isDebugEnabled())
        launch(Dispatchers.Default) {
            while (job.isActive) {
                val count = AtomicInteger()
                countChildren(count, job)

                logger.debug { "----- Benchmarking task running with $count children" }
                delay(5000)
            }

            logger.debug { "Benchmarking task finished" }
        }

    // Get Version Information

    val versionsFolder = File(output, "versions")
    if (!versionsFolder.exists())
        versionsFolder.mkdirs()

    for (lang in languages)
        launch {
            val version = lang.absoluteVersion

            logger.info { "Retrieving verison information for '${lang.id}'" }

            val temp = Files.createTempDirectory(null).toFile()
            val res = version.runCommand(temp).run {
                if (this == null) return@run "Could not determine version"

                return@run trim()
            }
            temp.delete()

            val versionFile = File(versionsFolder, "${lang.id}.txt")
            if (versionFile.exists())
                versionFile.delete()

            logger.debug { "Writing version information for '${lang.id}' at ${versionFile.absolutePath}" }

            versionFile.createNewFile()
            versionFile.writeText(res)

            logger.info { "Retrieved version information for '${lang.id}'" }
        }
    
    // OS File
    
    launch {
        val osFile = File(versionsFolder, "os.txt")
        if (osFile.exists())
            osFile.delete()

        logger.debug { "Writing Platform information at ${osFile.absolutePath}" }

        osFile.createNewFile()

        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz").format(Date())
        val si = SystemInfo()

        // Hardware Information
        fun Long.toBytes(): String {
            val kb = this / 1_000.0
            val mb = kb / 1_000.0
            val gb = mb / 1_000.0
            val tb = gb / 1_000.0

            return when {
                tb > 1 -> "${String.format("%,.3f", tb)}TB"
                gb > 1 -> "${String.format("%,.3f", gb)}GB"
                mb > 1 -> "${String.format("%,.3f", mb)}MB"
                kb > 1 -> "${String.format("%,.3f", kb)}KB"
                else -> "${this}B"
            }
        }

        fun Long.toFrequency(): String {
            val ghz = this / 1_000_000_000.0
            val mhz = this / 1_000_000.0
            return if (ghz > 0) "${String.format("%,.3f", ghz)}GHz" else "${String.format("%,.3f", mhz)}MHz"
        }

        val memory = si.hardware.memory
        val physicalMemory = memory.physicalMemory.joinToString("\n") {
            "- ${it.capacity.toBytes()} ${it.memoryType} (${it.bankLabel}) @ ${it.clockSpeed.toFrequency()} #${it.partNumber}"
        }

        val disks = si.hardware.diskStores.joinToString("\n") {
            "- ${it.name} (${it.model}) - ${it.size.toBytes()}"
        }

        // JVM Information

        val jvmMaxMemory = Runtime.getRuntime().maxMemory().run {
            val mb = this / 1024 / 1024
            val gb = mb / 1024
            if (gb > 0) "${gb}GB" else "${mb}MB"
        }

        val cpu = si.hardware.processor.processorIdentifier

        val text = """
            $date
            $os-$arch ${System.getProperty("os.version")}
            ${si.operatingSystem.family} - ${si.operatingSystem.manufacturer}
            ${si.hardware.computerSystem.model} - ${si.hardware.computerSystem.manufacturer}
            ${si.operatingSystem.bitness} Bit
            ---
            CPU: ${cpu.name} ${cpu.model.trim()}
            CPU Identifier: ${cpu.identifier}
            CPU Vendor: ${cpu.vendor}
            CPU Microarchitecture: ${cpu.microarchitecture}
            CPU Frequency: ${si.hardware.processor.maxFreq.toFrequency()}
            CPU Processors: ${si.hardware.processor.logicalProcessorCount}
            ---
            Memory Page Size: ${memory.pageSize.toBytes()}
            Max Memory: ${memory.total.toBytes()}
            Max Virtual Memory: ${memory.virtualMemory.virtualMax.toBytes()}
            Physical Memory:
            $physicalMemory
            ---
            Disks:
            $disks
            ---
            JVM Max Memory: $jvmMaxMemory
            Available CPU Cores: ${Runtime.getRuntime().availableProcessors()}
            """.lines().joinToString("\n") { it.trim() }

        logger.debug { "Platform information:\n$text" }

        osFile.writeText(text)

        logger.debug { "Wrote Platform information to ${osFile.absolutePath}" }
    }
}

fun CoroutineScope.runBenchmark(language: Language, folder: File, out: File) = async(Dispatchers.IO) {
    val configFile = File(folder, "config.yml").readText(Charsets.UTF_8)
    val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)

    logger.debug { "Starting benchmark '${config.name}' for '${language.id}'" }

    if (config.disabled.contains(language.id)) {
        logger.info { "Language '${language.id}' is disabled for '${config.name}'" }
        return@async null
    }

    val results = mutableListOf<Double>()

    coroutineScope {
        var run = language.absoluteRun

        if (language.file)
            run = "${folder.absolutePath}${s}$run"

        logger.debug { "Running Command for '${language.id}': '$run' in ${folder.absolutePath}" }

        val jobs = mutableListOf<Job>()

        // Initialize - Removes Outliers
        run.runCommand(folder)
        logger.debug { "Initialized benchmark for '${language.id}' in ${folder.name}" }

        repeat(RUN_COUNT) { i ->
            val job = async {
                var runTime0 = run.runCommand(folder)!!.trim().replace("[\\s\\n]+".toRegex(), "")
                var tries = 0
                while (runTime0.isEmpty()) {
                    if (tries > EMPTY_RETRY_COUNT)
                        error("Failed to run command '$run' for '${language.id}' in ${folder.name} (#${i + 1}): Found empty output $EMPTY_RETRY_COUNT times")

                    // Try again
                    logger.debug { "Empty output for '${language.id}' in ${folder.name} (#${i + 1})" }
                    runTime0 = run.runCommand(folder)!!.trim().replace("[\\s\\n]+".toRegex(), "")
                    tries++
                }

                val runTime = runTime0.toDoubleOrNull() ?: error("Failed to parse output: '$runTime0'")

                logger.debug { "${language.language} Run in '${folder.name}': $runTime${config.measure.unit} (#${i + 1})" }

                return@async runTime
            }

            jobs.add(job)
            results.add(job.await())
        }

        if (logger.isDebugEnabled())
            launch {
                while (jobs.any { it.isActive }) {
                    val active = jobs.count { it.isActive }
                    val completed = jobs.count { it.isCompleted }
                    logger.debug { "Waiting for $active / ${jobs.size} jobs to finish for '${language.id}' on ${folder.name} ($completed completed)" }
                    delay(5000)
                }
            }
    }

    logger.debug {
        """
        --
        ---
        Finished running benchmark commands for '${language.id}' in ${folder.absolutePath}
        Had ${config.disabled.size} disabled languages
        Collected ${results.size} results
        -----
        """.trimIndent()
    }

    if (results.isEmpty())
        error("No results found for '${config.name}' on ${language.language}")

    val id = folder.name
    val data = BenchmarkResult(id, language, config, results)

    val file = File(out, "$id.json")
    logger.info { "Writing to ${file.absolutePath}" }

    if (file.exists())
        file.delete()

    if (!file.exists())
        file.createNewFile()

    file.writeText(json.encodeToString(data))
    return@async data
}

suspend fun rankBenchmarks(results: Map<String, List<BenchmarkResult>>, out: File) = coroutineScope {
    val obj = buildJsonObject {
        val scores = mutableMapOf<String, Int>()

        for ((name, results) in results) {
            val sorted = results.sortedBy { it.results.sum() }

            put(name, buildJsonObject {
                put("summary", JsonArray(sorted.map { JsonPrimitive(it.languageName) }.toList()))
                put("verbose", buildJsonObject {
                    for ((i, result) in sorted.withIndex()) {
                        scores[result.languageName] = scores.getOrDefault(result.languageName, 0) + i
                        put(result.languageName, buildJsonObject {
                            put("rank", i + 1)
                            put("id", result.languageId)
                            put("avg", result.avg)
                            put("low", result.low)
                            put("high", result.high)
                        })
                    }
                })
            })
        }

        put("overall", buildJsonObject {
            val sorted = scores.toList().sortedBy { it.second }
            put("summary", JsonArray(sorted.map { JsonPrimitive(it.first) }.toList()))
            put("verbose", buildJsonObject {
                for ((i, pair) in sorted.withIndex()) {
                    put(pair.first, buildJsonObject {
                        put("rank", i + 1)
                        put("score", pair.second)
                    })
                }
            })
        })
    }

    val file = File(out, "rankings.json")
    logger.info { "Writing rankings to ${file.absolutePath}" }

    withContext(Dispatchers.IO) {
        if (!file.exists())
            file.createNewFile()

        file.writeText(json.encodeToString(obj))
    }
}