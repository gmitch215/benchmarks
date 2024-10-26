#include <iostream>
#include <vector>
#include <chrono>

int main() {
    const auto start = std::chrono::steady_clock::now();

    int arr[1000];
    for (int i = 0; i < 1000; i++) {
        arr[i] = i;
    }

    const auto end = std::chrono::steady_clock::now();
    const auto duration = end - start;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}