#include <stdio.h>
#include <stdlib.h>

#include "../../lib/c/nanotime/nanotime.h"

int main() {
    uint64_t before = nanotime_now();

    FILE *file = fopen("file.txt", "r");

    char ch;
    while ((ch = fgetc(file)) != EOF) {
        // Do nothing
    }

    fclose(file);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}