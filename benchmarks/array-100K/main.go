package main

import "fmt"
import "time"

func main() {
	var before = time.Now().UnixNano()

	var list = make([]int, 100_000)
	for i := 0; i < 100_000; i++ {
		list[i] = i
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
