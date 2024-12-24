import kotlin.time.measureTime

fun merge(arr: IntArray, l: Int, m: Int, r: Int) {
    val n1 = m - l + 1
    val n2 = r - m

    val L = IntArray(n1)
    val R = IntArray(n2)

    for (i in 0 until n1)
        L[i] = arr[l + i]

    for (j in 0 until n2)
        R[j] = arr[m + 1 + j]

    var i = 0; var j = 0
    var k = l

    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        } else {
            arr[k] = R[j];
            j++;
        }

        k++;
    }

    while (i < n1) {
        arr[k] = L[i];
        i++;
        k++;
    }

    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
}

fun mergeSort(arr: IntArray, l: Int, r: Int) {
    if (l < r) {
        val m = l + (r - l) / 2
        mergeSort(arr, l, m)
        mergeSort(arr, m + 1, r)
        merge(arr, l, m, r)
    }
}

fun main() {
    val time = measureTime {
        val arr = intArrayOf(312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984)
        mergeSort(arr, 0, arr.size - 1)
    }

    println(time.inWholeNanoseconds)
}