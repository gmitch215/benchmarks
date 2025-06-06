import kotlin.time.measureTime
import kotlin.math.sqrt

fun main() {
    val time = measureTime {
        for (i in 0..<100_000) {
            sqrt(i.toDouble())
        }
    }

    println(time.inWholeNanoseconds)
}