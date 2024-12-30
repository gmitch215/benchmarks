import time
import hashlib

phrases = [
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
]

before = time.time_ns()

for phrase in phrases:
    hashlib.sha1(phrase.encode()).hexdigest()

after = time.time_ns()

print(after - before)
