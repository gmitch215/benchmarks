package main

import "fmt"
import "time"
import "math"

func is_prime(n int) bool {
	var start = 2
	var limit = math.Sqrt(float64(n))

	for start <= int(limit) {
		if n%start == 0 {
			return false
		}

		start++
	}

	return true
}

func main() {
	var before = time.Now().UnixNano()

	for i := 0; i < 100000; i++ {
		is_prime(i)
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
