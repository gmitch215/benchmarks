import kotlin.time.measureTime
import kotlin.math.pow

fun main() {
    val time = measureTime {
        for (i in 0..<1_000) {
            val y = 1.1 + (i / 1000.0)
            2.0.pow(y)
        }
    }

    println(time.inWholeNanoseconds)
}