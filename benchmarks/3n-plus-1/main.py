import time

def collatz_next(n):
    if n % 2 == 0:
        return n // 2
    else:
        return 3 * n + 1

before = time.time_ns()

n = 837799
while n != 1:
    n = collatz_next(n)

after = time.time_ns()

print(after - before)
