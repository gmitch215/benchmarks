import kotlin.time.measureTime

fun next(n: Long): Long {
    return if (n % 2 == 0L) n / 2 else 3 * n + 1
}

fun main() {
    val time = measureTime {
        var n = 837799L
        while (n != 1L) {
            n = next(n)
        }
    }

    println(time.inWholeNanoseconds)
}