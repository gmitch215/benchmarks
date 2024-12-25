#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    int n;
    for (int i = 0; i < 1000; i++) {
        n = rand();
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}