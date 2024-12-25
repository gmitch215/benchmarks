#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

uint64_t next(uint64_t n) {
    if (n % 2 == 0) {
        return n / 2;
    } else {
        return (3 * n) + 1;
    }
}

int main() {
    uint64_t before = nanotime_now();

    uint64_t n = 837799;
    while (n != 1) {
        n = next(n);
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}