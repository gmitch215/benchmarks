#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int linearSearch(int arr[], int x) {
    for (int i = 0; i < sizeof(arr); i++) {
        if (arr[i] == x) {
            return i;
        }
    }
    return -1;
}

int main() {
    int arr[] = {1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319, 1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357, 2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317, 657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884};

    uint64_t before = nanotime_now();

    linearSearch(arr, arr[4]);
    linearSearch(arr, arr[11]);
    linearSearch(arr, arr[29]);
    linearSearch(arr, arr[36]);
    linearSearch(arr, arr[42]);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}