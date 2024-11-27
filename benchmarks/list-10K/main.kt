import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        val arr = mutableListOf<Int>()
        for (i in 0..<10000) {
            arr.add(i)
        }
    }

    println(time.inWholeNanoseconds)
}