import kotlin.time.measureTime
import kotlin.math.cbrt

fun main() {
    val time = measureTime {
        for (i in 0..<100_000) {
            cbrt(i.toDouble())
        }
    }

    println(time.inWholeNanoseconds)
}