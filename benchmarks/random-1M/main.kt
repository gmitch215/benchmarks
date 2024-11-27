import kotlin.time.measureTime
import kotlin.random.Random

fun main() {
    val time = measureTime {
        var n = 0.0;
        for (i in 0..<1000000) {
            n = Random.nextDouble()
        }
    }

    println(time.inWholeNanoseconds)
}