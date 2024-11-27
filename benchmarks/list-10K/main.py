import time

before = time.time_ns()

arr = []
for i in range(10000):
    arr.append(i)

after = time.time_ns()

print(after - before)
