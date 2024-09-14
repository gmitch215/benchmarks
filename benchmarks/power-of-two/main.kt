import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        var n = 0;
        for (i in 0..30) {
            n = 1 shl i
        }
    }

    println(time.inWholeNanoseconds)
}