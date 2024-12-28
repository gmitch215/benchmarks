#include <iostream>
#include <fstream>
#include <string>
#include <chrono>

int main() {
    const auto before = std::chrono::steady_clock::now();

    std::ifstream file("file.txt");
    std::string line;
    while (std::getline(file, line)) {
        // Do nothing
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}