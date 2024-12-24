import kotlin.time.measureTime

fun bubbleSort(arr: IntArray, n: Int) {
    var i = 0; var j = 0; var temp = 0
    var swapped = false
    for (i in 0 until n - 1) {
        swapped = false
        for (j in 0 until n - i - 1) {
            if (arr[j] > arr[j + 1]) {
                temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
                swapped = true
            }
        }

        if (swapped == false) break
    }
}

fun main() {
    val time = measureTime {
        val arr = intArrayOf(639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966)
        bubbleSort(arr, arr.size)
    }

    println(time.inWholeNanoseconds)
}