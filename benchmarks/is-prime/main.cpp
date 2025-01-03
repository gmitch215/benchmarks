#include <iostream>
#include <chrono>
#include <cmath>

bool is_prime(int n) {
    int start = 2;
    int limit = std::sqrt(n);

    while (start <= limit) {
        if (n % start++ == 0) return false;
    }

    return n > 1;
}

int main() {
    const auto before = std::chrono::steady_clock::now();

    for (int i = 0; i < 100000; i++) {
        is_prime(i);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}