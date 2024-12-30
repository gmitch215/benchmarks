#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../lib/c/nanotime/nanotime.h"
#include "../../lib/c/sha1/sha1.h"

static const char* phrases[] = {
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
    uint64_t before = nanotime_now();

    for (int i = 0; i < 25; i++) {
        uint8_t *result = malloc(20);
        SHA1((uint8_t *)phrases[i], strlen(phrases[i]), result);
        free(result);
    }

    uint64_t after = nanotime_now();

    printf("%lu", after - before);
    return 0;
}