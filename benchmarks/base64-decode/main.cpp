#include <iostream>
#include <chrono>
#include <string>

#include "../../lib/cpp-base64/include/base64.hpp"

int main() {
    std::string i1 = "SGVsbG8sIFdvcmxkIQ==";
    std::string i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    std::string i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    const auto before = std::chrono::steady_clock::now();

    base64::from_base64(i1);
    base64::from_base64(i2);
    base64::from_base64(i3);

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}