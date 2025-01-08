import time
import math

def jump_search(arr, x):
    n = len(arr)
    step = math.sqrt(n)
    prev = 0
    
    while arr[int(min(step, n) - 1)] < x:
        prev = step
        step += math.sqrt(n)
        if prev >= n:
            return -1
     
    while arr[int(prev)] < x:
        prev += 1
        if prev == min(step, n):
            return -1
     
    if arr[int(prev)] == x:
        return prev
     
    return -1

arr = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834]

before = time.time_ns()

jump_search(arr, arr[2])
jump_search(arr, arr[8])
jump_search(arr, arr[12])
jump_search(arr, arr[15])
jump_search(arr, arr[20])

after = time.time_ns()

print(after - before)
