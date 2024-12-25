#include <iostream>
#include <chrono>

#define HTTP_IMPLEMENTATION
#include "../../lib/c/clibs/http.h"

int main() {
    const auto before = std::chrono::steady_clock::now();

    http_t* req = http_get("http://httpbin.org/get", 0);
    if (!req) return 1;

    http_status_t status = HTTP_STATUS_PENDING;
    while (status == HTTP_STATUS_PENDING) {
    	status = http_process(req);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(duration).count() << std::endl;

    return 0;
}