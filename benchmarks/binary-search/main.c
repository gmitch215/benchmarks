#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int binarySearch(int arr[], int x) {
    int low = 0;
    int high = sizeof(arr) / sizeof(arr[0]) - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;

        if (arr[mid] == x)
            return mid;

        if (arr[mid] < x)
            low = mid + 1;

        else
            high = mid - 1;
    }

    return -1;
}

int main() {
    int arr[] = {2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834};

    uint64_t before = nanotime_now();

    binarySearch(arr, arr[2]);
    binarySearch(arr, arr[8]);
    binarySearch(arr, arr[12]);
    binarySearch(arr, arr[15]);
    binarySearch(arr, arr[20]);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}