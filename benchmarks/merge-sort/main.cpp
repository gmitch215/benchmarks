#include <iostream>
#include <chrono>
#include <vector>

void merge(std::vector<int>& arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    std::vector<int> L(n1), R(n2);

    for (int i = 0; i < n1; i++)
        L[i] = arr[left + i];

    for (int j = 0; j < n2; j++)
        R[j] = arr[mid + 1 + j];

    int i = 0, j = 0;
    int k = left;

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

void mergeSort(std::vector<int>& arr, int left, int right) {
    if (left >= right) return;

    int mid = left + (right - left) / 2;
    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}

int main() {
    const auto before = std::chrono::steady_clock::now();

    std::vector<int> arr = {312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984};
    mergeSort(arr, 0, arr.size() - 1);

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}