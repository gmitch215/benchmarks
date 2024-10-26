import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        val arr = arrayOfNulls<Int>(100_000)
        for (i in 0..<100_000) {
            arr[i] = i
        }
    }

    println(time.inWholeNanoseconds)
}