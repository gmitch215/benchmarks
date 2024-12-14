package xyz.gmitch215.benchmarks

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

val logger = KotlinLogging.logger("Benchmarks")

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

    constructor(id: String, run: BenchmarkRun, config: BenchmarkConfiguration, results: List<Double>) : this(
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

fun Job.cancelAfter(sec: Long) = CoroutineScope(EmptyCoroutineContext).launch {
    var t = 0
    while (t < sec) {
        if (this@cancelAfter.isCompleted) return@launch
        delay(1000)
        t++
    }

    if (this@cancelAfter.isCompleted) return@launch

    logger.debug { "Cancelling Job after ${sec}s" }
    this@cancelAfter.cancel()
}