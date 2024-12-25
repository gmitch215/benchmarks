package xyz.gmitch215.benchmarks

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.Language
import java.io.File
import java.util.*

val logger = KotlinLogging.logger("Benchmarks")

val os = System.getProperty("os.name").substringBefore(" ").lowercase()
val arch = when(System.getProperty("os.arch").lowercase()) {
    "amd64", "x86_64" -> "x64"
    "aarch64", "arm", "arm64" -> "arm64"
    "x86" -> "x86"
    else -> error("Unsupported architecture")
}
val kotlinNativeSuffix = if (os == "windows") ".exe" else ".kexe"
val s: String = File.separator ?: "/"

private const val NANO = "ns"
private const val MICRO = "µs"
private const val MILLI = "ms"
private const val SEC = "s"
private const val MIN = "m"

@Serializable
enum class Measurement(val multiplier: Double, val unit: String) {

    NANOSECONDS(1.0, NANO),

    MICROSECONDS(1_000.0, MICRO),

    MILLISECONDS(1_000_000.0, MILLI),

    SECONDS(1_000_000_000.0, SEC),

    MINUTES(60_000_000_000.0, MIN),

    ;

    fun formatOutput(number: Number, places: Int = 3, round: Boolean = false): String {
        var num = number.toDouble()
        var unit = this.unit
        if (round) {
            while (num >= 1000) {
                num /= 1000
                unit = when (unit) {
                    NANO -> MICRO
                    MICRO -> MILLI
                    MILLI -> SEC
                    SEC -> MIN
                    else -> MIN
                }
            }

            if (num <= 0.1) {
                num *= 1000
                unit = when (unit) {
                    MIN -> SEC
                    SEC -> MILLI
                    MILLI -> MICRO
                    MICRO -> NANO
                    else -> NANO
                }
            }
        }

        val value = "%,.${places}f".format(Locale.US, num)
        return "$value$unit"
    }

    fun formatOutput(number: Number, output: Measurement, places: Int = 3, floor: Boolean = false): String = output.formatOutput(convert(number, output), places, floor)

    fun convert(number: Number, output: Measurement): Double = number.toDouble() * multiplier / output.multiplier

    fun fromString(value: String): Double = value.replace(",", "").substringBeforeLast(unit).toDouble()

}

@Serializable
data class BenchmarkResult(
    val id: String,
    val name: String,
    val languageId: String,
    val languageName: String,
    val languageColor: String,
    val description: String,
    val measure: Measurement,
    val output: Measurement,
    val low: String,
    val high: String,
    val avg: String,
    val results: List<Double>
) {

    val avgDouble: Double
        get() = output.fromString(avg)

    val lowDouble: Double
        get() = output.fromString(low)

    val highDouble: Double
        get() = output.fromString(high)

    constructor(id: String, run: Language, config: BenchmarkConfiguration, results: List<Double>) : this(
        id,
        config.name,
        run.id,
        run.language,
        run.color,
        config.description,
        config.measure,
        config.output,
        config.measure.formatOutput(results.min(), config.output),
        config.measure.formatOutput(results.max(), config.output),
        config.measure.formatOutput(results.average(), config.output),
        results
    )

}

suspend fun String.runCommand(folder: File, pipe: Boolean = false): String? = coroutineScope {
    val str = this@runCommand
    var exitCode = -1

    try {
        val parts = split("\\s".toRegex())
        val builder = ProcessBuilder(*parts.toTypedArray())
            .directory(folder)

        if (pipe) builder.inheritIO()

        val process = builder.start()

        val waiting = launch {
            if (!logger.isDebugEnabled()) return@launch

            while (process.isAlive) {
                logger.debug { "Process '$str' is still running in ${folder.absolutePath}" }
                delay(5000)
            }
        }

        process.waitFor()
        waiting.cancel("Process finished")
        logger.debug { "Process '$str' finished in ${folder.absolutePath}" }

        exitCode = process.exitValue()
        if (exitCode != 0) {
            logger.error { "Failed to run command: '$str' in ${folder.absolutePath} with exit code $exitCode" }
            error(process.errorStream.bufferedReader().use { it.readText() })
        }

        val stdout = process.inputStream.bufferedReader().use { it.readText() }
        val stderr = process.errorStream.bufferedReader().use { it.readText() }

        if (stderr.isEmpty())
            return@coroutineScope stdout
        else
            return@coroutineScope stdout + "\n" + stderr
    } catch (e: Exception) {
        logger.error(e) { "Failed to run command: '$str' in ${folder.absolutePath}; exit code $exitCode" }
        throw IllegalStateException("Failed to run command: '$str' in ${folder.absolutePath}; exit code $exitCode", e)
    }
}