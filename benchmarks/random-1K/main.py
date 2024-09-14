import time
import random

before = time.time() * 1000

n = 0
for i in range(0, 1000):
    n = random.random()

after = time.time() * 1000

print(after - before)
