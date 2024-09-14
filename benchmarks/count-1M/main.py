import time

before = time.time_ns()

count = 0
for i in range(1_000_000):
    count += 1

after = time.time_ns()

print(after - before)
