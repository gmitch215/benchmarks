import time
import math

before = time.time_ns()

for i in range(1_000):
    math.cbrt(i)

after = time.time_ns()

print(after - before)
