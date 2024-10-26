import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        val arr = arrayOfNulls<Int>(1000)
        for (i in 0..<1_000) {
            arr[i] = i
        }
    }

    println(time.inWholeNanoseconds)
}