#include <iostream>
#include <vector>
#include <chrono>

int main() {
    const auto before = std::chrono::steady_clock::now();

    std::vector<int> arr;
    for (int i = 0; i < 10000; i++) {
        arr.push_back(i);
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}