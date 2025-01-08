#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int min(int a, int b){
    if (b > a)
        return a;
    else
      return b;
}

int jumpSearch(int arr[], int x) {
    int n = sizeof(arr) / sizeof(arr[0]);
    int step = sqrt(n);
    int prev = 0;

    while (arr[min(step, n)-1] < x) {
        prev = step;
        step += sqrt(n);
        if (prev >= n)
            return -1;
    }
 
    while (arr[prev] < x) {
        prev++;
        if (prev == min(step, n))
            return -1;
    }

    if (arr[prev] == x)
        return prev;
 
    return -1;
}

int main() {
    int arr[] = {2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834};

    uint64_t before = nanotime_now();

    jumpSearch(arr, arr[2]);
    jumpSearch(arr, arr[8]);
    jumpSearch(arr, arr[12]);
    jumpSearch(arr, arr[15]);
    jumpSearch(arr, arr[20]);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}