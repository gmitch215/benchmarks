#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "../../lib/c/nanotime/nanotime.h"

int is_prime(int n) {
    int start = 2;
    int limit = sqrt(n);

    while (start <= limit) {
        if (n % start++ == 0) return 0;
    }

    return n > 1;
}

int main() {
    uint64_t before = nanotime_now();

    for (int i = 0; i < 100000; i++) {
        is_prime(i);
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}