#include <iostream>
#include <chrono>

int main() {
    const auto start = std::chrono::steady_clock::now();

    int count = 0;
    for (int i = 0; i < 1000000; i++) {
        count++;
    }

    const auto end = std::chrono::steady_clock::now();
    const auto duration = end - start;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}