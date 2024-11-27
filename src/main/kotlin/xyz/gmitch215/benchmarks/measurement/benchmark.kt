@file:JvmName("Benchmarker")

package xyz.gmitch215.benchmarks.measurement

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import xyz.gmitch215.benchmarks.BenchmarkResult
import xyz.gmitch215.benchmarks.Measurement
import xyz.gmitch215.benchmarks.logger
import java.io.File

const val RUN_COUNT = 25
val json = Json {
    prettyPrint = true
}

private val os = System.getProperty("os.name").substringBefore(" ").lowercase()
private val kotlinNativeSuffix = if (os == "windows") ".exe" else ".kexe"

suspend fun main(args: Array<String>) = coroutineScope {
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
    launch {
        for (benchmark in benchmarkRuns) {
            val out = File(output, benchmark.id)
            if (!out.exists())
                out.mkdirs()

            logger.debug { "Running benchmark '${benchmark.id}'" }

            launch {
                for (f in folders) {
                    val name = f.name
                    if (filter != null && !filter.matches(name))
                        continue

                    launch {
                        val result = runBenchmark(benchmark, f, out)
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
    rankBenchmarks(results, output)
}

suspend fun runBenchmark(benchmarkRun: BenchmarkRun, folder: File, out: File) = withContext(Dispatchers.IO) {
    val configFile = File(folder, "config.yml").readText(Charsets.UTF_8)
    val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)

    val results = mutableListOf<Double>()

    launch {
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

            logger.debug { "Running Compile Command for '${benchmarkRun.id}': '$compile'" }

            val res = compile.runCommand(folder)
            if (res != null)
                logger.debug { "Compile result: $res" }
        }

        var run = benchmarkRun.run

        if (benchmarkRun.file)
            run = "${folder.absolutePath}${s}$run"

        if (benchmarkRun.id == "kotlin-native")
            run += kotlinNativeSuffix

        logger.debug { "Running Command for '${benchmarkRun.id}': '$run'" }

        for (i in 0 until RUN_COUNT)
            launch {
                val runTime0 = run.runCommand(folder)!!.trim().replace("[\\s\\n]+".toRegex(), "")
                val runTime = runTime0.toDoubleOrNull() ?: error("Failed to parse output: '$runTime0'")
                results.add(runTime)
            }
    }.join()

    if (benchmarkRun.cleanup != null)
        for (cleanup in benchmarkRun.cleanup) {
            val file = File(folder, cleanup)
            if (file.exists())
                file.delete()
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

private fun String.runCommand(folder: File): String? {
    try {
        val parts = split("\\s".toRegex())
        val process = ProcessBuilder(*parts.toTypedArray())
            .directory(folder)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        process.waitFor()

        if (process.exitValue() != 0)
            error(process.errorStream.bufferedReader().use { it.readText() })

        return process.inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        throw IllegalStateException("Failed to run command: '$this'", e)
    }
}

// Classes

@Serializable
data class BenchmarkConfiguration(
    val name: String,
    val description: String,
    val measure: Measurement,
    val output: Measurement
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
    val cleanup: List<String>? = null
)