package xyz.gmitch215.benchmarks

import kotlinx.serialization.Serializable
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import java.util.*

@Serializable
enum class Measurement(val multiplier: Double, val unit: String) {

    NANOSECONDS(1.0, "ns"),

    MICROSECONDS(1_000.0, "µs"),

    MILLISECONDS(1_000_000.0, "ms"),

    SECONDS(1_000_000_000.0, "s"),

    MINUTES(60_000_000_000.0, "m"),

    ;

    fun formatOutput(number: Number, output: Measurement): String {
        val value = "%,.3f".format(Locale.US, number.toDouble() * multiplier / output.multiplier)
        return "$value${output.unit}"
    }

}

@Serializable
data class BenchmarkResult(
    val id: String,
    val name: String,
    val description: String,
    val measure: Measurement,
    val output: Measurement,
    val low: String,
    val high: String,
    val avg: String,
    val results: List<Double>
) {

    constructor(id: String, config: BenchmarkConfiguration, results: List<Double>) : this(
        id,
        config.name,
        config.description,
        config.measure,
        config.output,
        config.measure.formatOutput(results.min(), config.output),
        config.measure.formatOutput(results.max(), config.output),
        config.measure.formatOutput(results.average(), config.output),
        results
    )

}