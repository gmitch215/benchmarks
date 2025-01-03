import time

def is_prime(n):
    start = 2
    limit = n // 2
    while start <= limit:
        if n % start == 0:
            return False
        start += 1

    return True

before = time.time_ns()

for i in range(1, 100_000):
    is_prime(i)

after = time.time_ns()

print(after - before)
