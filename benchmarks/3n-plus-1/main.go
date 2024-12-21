package main

import "fmt"
import "time"

func next(n int) int {
	if n%2 == 0 {
		return n / 2
	} else {
		return 3*n + 1
	}
}

func main() {
	var before = time.Now().UnixNano()

	var n = 837799
	for n != 1 {
		n = next(n)
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
