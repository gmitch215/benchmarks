import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        var count = 0
        for (i in 1..100_000_000) {
            count++
        }
    }

    println(time.inWholeNanoseconds)
}