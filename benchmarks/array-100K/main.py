import time

before = time.time_ns()

arr = [None] * 100_000
for i in range(100_000):
    arr.insert(i, i)

after = time.time_ns()

print(after - before)
