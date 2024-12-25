#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    int* arr = malloc(0);
    for (int i = 0; i < 100; i++) {
        int* arrcpy = malloc((i + 1) * sizeof(int));

        for (int j = 0; j <= i; j++) {
            arrcpy[j] = arr[j];
        }

        free(arr);
        arr = arrcpy;
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}