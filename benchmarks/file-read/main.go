package main

import "fmt"
import "time"
import "os"

func main() {
	var before = time.Now().UnixNano()

	_, _ = os.ReadFile("file.txt")

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
