#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../lib/nanotime/nanotime.h"
#include "../../lib/c-base64/base64.h"

int main() {
    char* i1 = "SGVsbG8sIFdvcmxkIQ==";
    char* i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    char* i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    uint64_t before = nanotime_now();

    base64_decode(i1);
    base64_decode(i2);
    base64_decode(i3);

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}