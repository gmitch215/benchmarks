package main

import "fmt"
import "time"

func BinarySearch(arr []int, x int) int {
	start := 0
	end := len(arr) - 1

	for start <= end {
		mid := (start + end) / 2
		if arr[mid] == x {
			return mid
		} else if arr[mid] < x {
			start = mid + 1
		} else if arr[mid] > x {
			end = mid - 1
		}
	}

	return -1
}

func main() {
	var before = time.Now().UnixNano()

	arr := []int{2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834}

	BinarySearch(arr, arr[2])
	BinarySearch(arr, arr[8])
	BinarySearch(arr, arr[12])
	BinarySearch(arr, arr[15])
	BinarySearch(arr, arr[20])

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
