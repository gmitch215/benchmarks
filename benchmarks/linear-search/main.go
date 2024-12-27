package main

import "fmt"
import "time"

func LinearSearch(arr []int, target int) int {
	for i := 0; i < len(arr); i++ {
		if arr[i] == target {
			return i
		}
	}
	return -1
}

func main() {
	arr := []int{1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319, 1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357, 2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317, 657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884}

	var before = time.Now().UnixNano()

	LinearSearch(arr, arr[4])
	LinearSearch(arr, arr[11])
	LinearSearch(arr, arr[29])
	LinearSearch(arr, arr[36])
	LinearSearch(arr, arr[42])

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
