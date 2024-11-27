#include <stdio.h>
#include <stdlib.h>

#include "../../lib/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    int arr[100000];
    for (int i = 0; i < 100000; i++) {
        arr[i] = i;
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}