function merge(arr, left, mid, right) {
    const n1 = mid - left + 1;
    const n2 = right - mid;

    const L = new Array(n1);
    const R = new Array(n2);

    for (let i = 0; i < n1; i++)
        L[i] = arr[left + i];

    for (let j = 0; j < n2; j++)
        R[j] = arr[mid + 1 + j];

    let i = 0, j = 0;
    let k = left;

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

function mergeSort(arr, left, right) {
    if (left >= right)
        return;

    const mid = Math.floor(left + (right - left) / 2);
    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}

let before = process.hrtime.bigint()

let arr = Array(
    312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984
)
mergeSort(arr, 0, arr.length - 1)

let after = process.hrtime.bigint()

console.log((after - before).toString())