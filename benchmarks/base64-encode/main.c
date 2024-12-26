#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../lib/c/nanotime/nanotime.h"
#include "../../lib/c/base64/base64.h"

int main() {
    char* i1 = "Hello, World!";
    char* i2 = "The quick brown fox jumps over the lazy dog.";
    char* i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    uint64_t before = nanotime_now();

    base64_encode(i1);
    base64_encode(i2);
    base64_encode(i3);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}