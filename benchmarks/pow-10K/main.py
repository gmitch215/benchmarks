import time

before = time.time_ns()

for i in range(10_000):
    y = 1.1 + (i / 10000.0)
    pow(2, y)

after = time.time_ns()

print(after - before)
