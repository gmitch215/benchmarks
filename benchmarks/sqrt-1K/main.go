package main

import "fmt"
import "time"
import "math"

func main() {
	var before = time.Now().UnixNano()

	for i := 0; i < 1000; i++ {
		math.Sqrt(float64(i))
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
