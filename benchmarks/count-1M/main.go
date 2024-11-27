package main

import "fmt"
import "time"

func main() {
	var before = time.Now().UnixNano()

	var count = 0
	for i := 0; i < 1000000; i++ {
		count++
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
