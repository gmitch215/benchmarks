#include <stdio.h>
#include <stdlib.h>

#include "../../lib/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    int count;
    for (int i = 0; i < 1000000; i++) {
        count++;
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}