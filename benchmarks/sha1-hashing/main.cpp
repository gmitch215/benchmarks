#include <iostream>
#include <vector>
#include <string>
#include <chrono>

#include "../../lib/cpp/digest/algorithm/sha1.hpp"

static const std::vector<std::string> phrases = {
    "Seize the day",
    "Time flies quickly",
    "Reach for the stars",
    "Love conquers all",
    "Break the ice",
    "A blessing in disguise",
    "Actions speak louder than words",
    "Every cloud has a silver lining",
    "Burn the midnight oil",
    "Jack of all trades",
    "Piece of cake",
    "Bite the bullet",
    "Crystal clear",
    "Better late than never",
    "The early bird catches the worm",
    "Cat got your tongue",
    "A penny for your thoughts",
    "Laughter is the best medicine",
    "All ears",
    "Barking up the wrong tree",
    "Beauty is in the eye of the beholder",
    "The calm before the storm",
    "Let the cat out",
    "On thin ice",
    "Cut to the chase"
};

int main() {
    const auto before = std::chrono::steady_clock::now();

    digestpp::sha1 sha1;

    for (int i = 0; i < 25; i++) {
        sha1.absorb(phrases[i]);
        sha1.hexdigest();
    }

    const auto after = std::chrono::steady_clock::now();
    const auto duration = after - before;

    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count() << std::endl;

    return 0;
}