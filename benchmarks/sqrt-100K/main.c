#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "../../lib/c/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    for (int i = 0; i < 100000; i++) {
        double x = sqrt(i);
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}