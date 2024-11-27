#include <stdio.h>
#include <stdlib.h>

#include "../../lib/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    int n;
    for (int i = 0; i < 31; i++) {
        n = 1 << i;
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}