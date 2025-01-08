package main

import "fmt"
import "time"
import "math"

func JumpSearch(arr []int, target int) int {
	n := len(arr)
	if n == 0 {
		return -1
	}

	blockSize := int(math.Sqrt(float64(n)))
	prev := 0

	for arr[int(math.Min(float64(blockSize), float64(n)))-1] < target {
		prev = blockSize
		blockSize += int(math.Sqrt(float64(n)))
		if prev >= n {
			return -1
		}
	}

	for i := prev; i < int(math.Min(float64(blockSize), float64(n))); i++ {
		if arr[i] == target {
			return i
		}
	}

	return -1
}

func main() {
	arr := []int{2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834}

	var before = time.Now().UnixNano()

	JumpSearch(arr, arr[2])
	JumpSearch(arr, arr[8])
	JumpSearch(arr, arr[12])
	JumpSearch(arr, arr[15])
	JumpSearch(arr, arr[20])

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
