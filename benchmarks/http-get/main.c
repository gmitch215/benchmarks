#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define HTTP_IMPLEMENTATION
#include "../../lib/clibs/http.h"

int main() {
    clock_t before = (clock() * 1000) / CLOCKS_PER_SEC;

    http_t* req = http_get("http://httpbin.org/get", 0);
    if (!req) return 1;

    http_status_t status = HTTP_STATUS_PENDING;
    while (status == HTTP_STATUS_PENDING) {
    	status = http_process(req);
    }

    clock_t after = (clock() * 1000) / CLOCKS_PER_SEC;

    printf("%lu", after - before);
    return 0;
}