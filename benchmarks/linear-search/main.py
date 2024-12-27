import time

def linear_search(arr, target):
    for i in range(len(arr)):
        if arr[i] == target:
            return i

arr = [1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319, 1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357, 2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317, 657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884]

before = time.time_ns()

linear_search(arr, arr[4])
linear_search(arr, arr[11])
linear_search(arr, arr[29])
linear_search(arr, arr[36])
linear_search(arr, arr[42])

after = time.time_ns()

print(after - before)
