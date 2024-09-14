package xyz.gmitch215.benchmarks

import kotlinx.serialization.Serializable

@Serializable
enum class Measurement(val multiplier: Double, val unit: String) {

    NANOSECONDS(1.0, "ns"),

    MICROSECONDS(1_000.0, "µs"),

    MILLISECONDS(1_000_000.0, "ms"),

    SECONDS(1_000_000_000.0, "s"),

    MINUTES(60_000_000_000.0, "m"),

    ;

    fun formatOutput(number: Number, output: Measurement): String {
        val value = number.toDouble() * multiplier / output.multiplier
        return "$value${output.unit}"
    }

}