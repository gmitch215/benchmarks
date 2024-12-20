#include <iostream>
#include <chrono>
#include <cmath>

int main() {
    const auto before = std::chrono::steady_clock::now();

    for (int i = 0; i < 10000; i++) {
        double y = 1.1 + (i / 10000.0);
        pow(2, y);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}