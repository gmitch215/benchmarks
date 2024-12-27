import kotlin.time.measureTime

fun binarySearch(arr: IntArray, x: Int): Int {
    var low = 0
    var high = arr.size - 1

    while (low <= high) {
        val mid = low + (high - low) / 2

        if (arr[mid] == x) {
            return mid
        }

        if (arr[mid] < x) {
            low = mid + 1
        } else {
            high = mid - 1
        }
    }

    return -1
}

fun main() {
    val arr = intArrayOf(2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834)

    val time = measureTime {
        binarySearch(arr, arr[2])
        binarySearch(arr, arr[8])
        binarySearch(arr, arr[12])
        binarySearch(arr, arr[15])
        binarySearch(arr, arr[20])
    }

    println(time.inWholeNanoseconds)
}