import kotlin.math.sqrt
import kotlin.math.min
import kotlin.time.measureTime

fun jumpSearch(arr: IntArray, target: Int): Int {
    val n = arr.size
    if (n == 0) return -1

    var blockSize = sqrt(n.toDouble()).toInt()
    var prev = 0

    while (arr[min(blockSize, n) - 1] < target) {
        prev = blockSize
        if (prev >= n) return -1
        blockSize += sqrt(n.toDouble()).toInt()
    }

    for (i in prev until min(blockSize, n)) {
        if (arr[i] == target) return i
    }

    return -1
}

fun main() {
    val arr = intArrayOf(2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834)

    val time = measureTime {
        jumpSearch(arr, arr[2])
        jumpSearch(arr, arr[8])
        jumpSearch(arr, arr[12])
        jumpSearch(arr, arr[15])
        jumpSearch(arr, arr[20])
    }

    println(time.inWholeNanoseconds)
}