@file:JvmName("Benchmarker")

package xyz.gmitch215.benchmarks.measurement

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import xyz.gmitch215.benchmarks.BenchmarkResult
import xyz.gmitch215.benchmarks.Measurement
import xyz.gmitch215.benchmarks.logger
import java.io.File
import java.util.concurrent.TimeUnit

const val RUN_COUNT = 25
val json = Json {
    prettyPrint = true
}

private val os = System.getProperty("os.name").substringBefore(" ").lowercase()
private val arch = when(System.getProperty("os.arch").lowercase()) {
    "amd64", "x86_64" -> "x64"
    "aarch64", "arm", "arm64" -> "arm64"
    "x86" -> "x86"
    else -> error("Unsupported architecture")
}

private val kotlinNativeSuffix = if (os == "windows") ".exe" else ".kexe"

suspend fun main(args: Array<String>) = withContext(Dispatchers.IO) {
    val input = File(args[0])
    val output = File(input, "output")

    val filter = args.getOrNull(1)?.let { Regex(it) }
    if (filter != null)
        logger.info { "Filtering benchmarks with '$filter'" }

    val file = File(input, "config.yml").readText(Charsets.UTF_8)
    val benchmarkRuns = Yaml.default.decodeFromString<List<BenchmarkRun>>(file)

    logger.info { "Starting Benchmarks on $os" }

    val folders = input.listFiles { f -> f?.let { it.isDirectory && it.name != "output" } == true } ?: emptyArray()
    logger.debug { "Found ${folders.size} folders" }

    if (folders.isEmpty())
        error("No benchmarks found")

    // Run Benchmarks

    val results = mutableMapOf<String, List<BenchmarkResult>>()
    val disabledForRanking = mutableSetOf<String>()
    val mutex = Mutex()

    launch(Dispatchers.Default) {
        for (benchmark in benchmarkRuns) {
            val out = File(output, benchmark.id)
            if (!out.exists())
                out.mkdirs()

            logger.debug { "Starting language '${benchmark.id}'" }

            launch {
                for (f in folders) {
                    val name = f.name
                    if (filter != null && !filter.matches(name))
                        continue

                    logger.debug { "Starting benchmark '$name' for '${benchmark.id}'" }
                    launch {
                        val result = runBenchmark(benchmark, f, out)
                        if (result == null) {
                            mutex.withLock {
                                disabledForRanking.add(benchmark.id)
                            }

                            return@launch // Disabled Language
                        }

                        if (results.contains(name))
                            results[f.name] = results[f.name]!! + result
                        else
                            results[f.name] = listOf(result)
                    }
                }
            }
        }
    }.join()

    // Rank Benchmarks
    rankBenchmarks(results.filterKeys { it !in disabledForRanking }, output)
}

suspend fun runBenchmark(benchmarkRun: BenchmarkRun, folder: File, out: File) = withContext(Dispatchers.IO) {
    val configFile = File(folder, "config.yml").readText(Charsets.UTF_8)
    val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)

    if (config.disabled.contains(benchmarkRun.id)) return@withContext null

    val results = mutableListOf<Double>()
    val mutex = Mutex()

    launch(Dispatchers.Default) {
        val s = File.separator

        var compile = benchmarkRun.compile
        if (compile != null) {
            if (benchmarkRun.location != null) {
                val home = System.getenv(benchmarkRun.location)
                if (home != null)
                    compile = "${home}${s}bin${s}$compile"

                if (os == "windows") {
                    val executableSuffix = if (benchmarkRun.id.contains("kotlin")) ".bat" else ".exe"
                    compile = compile.replaceFirst(" ", "$executableSuffix ")
                }
            }

            if (benchmarkRun.compileExtra.isNotEmpty()) {
                val extra = benchmarkRun.compileExtra["$os-$arch"] ?: benchmarkRun.compileExtra[os]

                if (extra != null)
                    compile += " $extra"
            }

            logger.debug { "Running Compile Command for '${benchmarkRun.id}': '$compile' in ${folder.absolutePath}" }

            val res = compile.runCommand(folder)
            if (res != null && res.isNotEmpty())
                logger.debug { "Compile result: $res" }
        }

        var run = benchmarkRun.run

        if (benchmarkRun.file)
            run = "${folder.absolutePath}${s}$run"

        if (benchmarkRun.id == "kotlin-native")
            run += kotlinNativeSuffix

        logger.debug { "Running Command for '${benchmarkRun.id}': '$run' in ${folder.absolutePath}" }

        for (i in 0 until RUN_COUNT)
            launch {
                val runTime0 = run.runCommand(folder)!!.trim().replace("[\\s\\n]+".toRegex(), "")
                val runTime = runTime0.toDoubleOrNull() ?: error("Failed to parse output: '$runTime0'")

                mutex.withLock {
                    results.add(runTime)
                }
            }
    }.join()

    if (benchmarkRun.cleanup != null)
        for (cleanup in benchmarkRun.cleanup) {
            val file = File(folder, cleanup)
            if (file.exists()) {
                file.delete()
                logger.debug { "Deleted file from cleanup: ${file.absolutePath}" }
            }
        }

    if (results.isEmpty())
        error("No results found for '${config.name}' on ${benchmarkRun.language}")

    val id = folder.name
    val data = BenchmarkResult(id, benchmarkRun, config, results)

    val file = File(out, "$id.json")
    logger.info { "Writing to ${file.absolutePath}" }

    if (!file.exists())
        file.createNewFile()

    file.writeText(json.encodeToString(data))

    return@withContext data
}

suspend fun rankBenchmarks(results: Map<String, List<BenchmarkResult>>, out: File) = coroutineScope {
    val obj = buildJsonObject {
        val scores = mutableMapOf<String, Int>()

        launch {
            for ((name, results) in results) {
                launch {
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
            }
        }.join()

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

private suspend fun String.runCommand(folder: File): String? = coroutineScope {
    val str = this@runCommand

    try {
        val parts = split("\\s".toRegex())
        val process = ProcessBuilder(*parts.toTypedArray())
            .directory(folder)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        val success = process.waitFor(120, TimeUnit.SECONDS)
        if (!success)
            error("Process timed out: '$str' in ${folder.absolutePath}")

        val exitCode = process.exitValue()

        if (exitCode != 0) {
            logger.error { "Failed to run command: '$str' in ${folder.absolutePath} with exit code $exitCode" }
            error(process.errorStream.bufferedReader().use { it.readText() })
        }

        return@coroutineScope process.inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        logger.error(e) { "Failed to run command: '$str' in ${folder.absolutePath}" }
        throw IllegalStateException("Failed to run command: '$str'", e)
    }
}

// Classes

@Serializable
data class BenchmarkConfiguration(
    val name: String,
    var id: String = name,
    val description: String,
    val measure: Measurement,
    val output: Measurement,
    val tags: List<String> = emptyList(),
    val disabled: List<String> = emptyList()
)

@Serializable
data class BenchmarkRun(
    val language: String,
    val id: String,
    val color: String,
    val file: Boolean = false,
    val location: String? = null,
    val run: String,
    val compile: String? = null,
    @SerialName("compile-extra")
    val compileExtra: Map<String, String> = emptyMap(),
    val cleanup: List<String>? = null,
)