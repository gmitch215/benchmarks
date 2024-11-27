package main

import "fmt"
import "time"

func main() {
	var before = time.Now().UnixNano()

	var list = make([]int, 0)
	for i := 0; i < 100; i++ {
		list = append(list, i)
	}

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
