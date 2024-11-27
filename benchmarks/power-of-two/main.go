package main

import "fmt"
import "time"

func main() {
	var before = time.Now().UnixNano()

	var n = 0
	for i := 0; i < 30; i++ {
		n = 1 << i
	}
	n = n

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
