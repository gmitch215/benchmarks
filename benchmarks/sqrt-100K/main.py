import time
import math

before = time.time_ns()

for i in range(100_000):
    math.sqrt(i)

after = time.time_ns()

print(after - before)