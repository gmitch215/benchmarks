#include <iostream>
#include <chrono>
#include <string>

#include "../../lib/cpp/base64/include/base64.hpp"

int main() {
    const auto before = std::chrono::steady_clock::now();

    std::string i1 = "Hello, World!";
    std::string i2 = "The quick brown fox jumps over the lazy dog.";
    std::string i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    base64::to_base64(i1);
    base64::to_base64(i2);
    base64::to_base64(i3);

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}