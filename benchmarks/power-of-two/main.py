import time

before = time.time_ns()

n = 0
for i in range(0, 30):
    n = 1 << i

after = time.time_ns()

print(after - before)
