package main

import (
	"fmt"
	"math"
)
import "time"

func main() {
	var before = time.Now().UnixNano()

	for i := 0; i < 100000; i++ {
		math.Sqrt(float64(i))
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
