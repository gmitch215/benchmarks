#include <iostream>
#include <chrono>

uint64_t next(uint64_t n) {
    if (n % 2 == 0) {
        return n / 2;
    } else {
        return (3 * n) + 1;
    }
}

int main() {
    const auto before = std::chrono::steady_clock::now();

    uint64_t n = 837799;
    while (n != 1) {
        n = next(n);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}