#include <iostream>
#include <chrono>

int main() {
    const auto before = std::chrono::steady_clock::now();

    int arr[100000];
    for (int i = 0; i < 100000; i++) {
        arr[i] = i;
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}