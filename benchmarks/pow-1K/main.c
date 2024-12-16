#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "../../lib/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    for (int i = 0; i < 1000; i++) {
        double y = 1.1 + (i / 1000.0);
        pow(2, y);
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}