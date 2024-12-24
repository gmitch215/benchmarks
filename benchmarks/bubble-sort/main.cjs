function bubbleSort(arr, n) {
    let i, j, temp
    let swapped
    for (i = 0; i < n - 1; i++) {
        swapped = false
        for (j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
                swapped = true
            }
        }

        if (swapped === false) break
    }
}

let before = process.hrtime.bigint()

let arr = Array(
    639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966
)
bubbleSort(arr, arr.length)

let after = process.hrtime.bigint()

console.log((after - before).toString())