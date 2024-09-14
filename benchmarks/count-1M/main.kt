import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        var count = 0
        for (i in 0..1_000_000) {
            count++
        }
    }

    println(time.inWholeNanoseconds)
}