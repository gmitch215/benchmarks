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
import java.io.File

private const val RUN_COUNT = 25

private val os = System.getProperty("os.name").substringBefore(" ").lowercase()
private val kotlinNativeSuffix = if (os == "windows") ".exe" else ".kexe"
private val json = Json {
    prettyPrint = true
}

suspend fun main(args: Array<String>) = coroutineScope {
    val input = File(args[0])
    val output = File(input, "output")

    val file = File(input, "config.yml").readText(Charsets.UTF_8)
    val benchmarkRuns = Yaml.default.decodeFromString<List<BenchmarkRun>>(file)

    println("Starting Benchmarks on $os")

    val folders = input.listFiles { f -> f?.let { it.isDirectory && it.name != "output" } ?: false } ?: emptyArray()

    if (folders.isEmpty())
        error("No benchmarks found")

    for (benchmark in benchmarkRuns) {
        val out = File(output, benchmark.id)
        if (!out.exists())
            out.mkdirs()

        launch {
            for (f in folders)
                runBenchmark(benchmark, f, out)
        }
    }
}

suspend fun runBenchmark(benchmarkRun: BenchmarkRun, folder: File, out: File) = withContext(Dispatchers.IO) {
    val configFile = File(folder, "config.yml").readText(Charsets.UTF_8)
    val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)

    val results = mutableListOf<Double>()

    launch(Dispatchers.Default) {
        var compile = benchmarkRun.compile
        val s = File.separator

        if (compile != null) {
            if (benchmarkRun.location != null) {
                val home = System.getenv(benchmarkRun.location)
                if (home != null)
                    compile = "${home}${s}bin${s}$compile"
            }

            if (os == "windows") {
                val executableSuffix = if (benchmarkRun.id.contains("kotlin")) ".bat" else ".exe"
                compile = compile.replaceFirst(" ", "$executableSuffix ")
            }

            compile.runCommand(folder)
        }

        var run = benchmarkRun.run
        if (benchmarkRun.id == "kotlin-native")
            run = "$folder${s}$run$kotlinNativeSuffix"

        for (i in 0 until RUN_COUNT)
            launch {
                val runTime0 = run.runCommand(folder)!!
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
    val data = BenchmarkResult(id, config, results)

    val file = File(out, "$id.json")
    println("Writing to ${file.absolutePath}")

    if (!file.exists())
        file.createNewFile()

    file.writeText(json.encodeToString(data))
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
        e.printStackTrace()
        return null
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
    val location: String? = null,
    val run: String,
    val compile: String? = null,
    val cleanup: List<String>? = null
)