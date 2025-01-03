import kotlin.time.measureTime
import kotlin.math.sqrt

fun isPrime(n: Int): Boolean {
    var start = 2;
    val limit = sqrt(n.toDouble())

    while (start <= limit) {
        if (n % start == 0) return false
        start++
    }

    return n > 1
}

fun main() {
    val time = measureTime {
        for (i in 0 until 100_000) {
            isPrime(i)
        }
    }

    println(time.inWholeNanoseconds)
}