import time

before = time.time_ns()

for i in range(1_000):
    y = 1.1 + (i / 1000.0)
    pow(2, y)

after = time.time_ns()

print(after - before)
