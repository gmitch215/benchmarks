import time
import random

before = time.time_ns()

n = 0
for i in range(0, 1000000):
    n = random.random()

after = time.time_ns()

print(after - before)
