function binarySearch(arr, x) {
    let low = 0
    let high = arr.length - 1
    let mid

    while (high >= low) {
        mid = low + Math.floor((high - low) / 2)

        if (arr[mid] === x)
            return mid

        if (arr[mid] > x)
            high = mid - 1
        else
            low = mid + 1
    }

    return -1
}

let arr = Array(
    2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834
)

let before = process.hrtime.bigint()

binarySearch(arr, arr[2])
binarySearch(arr, arr[8])
binarySearch(arr, arr[12])
binarySearch(arr, arr[15])
binarySearch(arr, arr[20])

let after = process.hrtime.bigint()

console.log((after - before).toString())