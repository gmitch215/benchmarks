package main

import (
	"fmt"
	"math"
)
import "time"

func main() {
	var before = time.Now().UnixNano()

	for i := 0; i < 1000; i++ {
		var y = 1.1 + (float64(i) / 1000.0)
		math.Pow(2, y)
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
