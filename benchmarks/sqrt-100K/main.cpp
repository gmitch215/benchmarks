#include <iostream>
#include <chrono>
#include <cmath>

int main() {
    const auto before = std::chrono::steady_clock::now();

    for (int i = 0; i < 100000; i++) {
        double x = std::sqrt(i);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}