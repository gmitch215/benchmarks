package main

import "fmt"
import "time"
import "math/rand"

func main() {
	var before = time.Now().UnixNano()

	var n = 0.0
	for i := 0; i < 1_000_000; i++ {
		n = rand.Float64()
	}
	n = n

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
