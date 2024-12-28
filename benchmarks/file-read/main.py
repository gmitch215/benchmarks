import time

before = time.time_ns()

with open("file.txt", "r") as file:
    file.read()

after = time.time_ns()

print(after - before)
