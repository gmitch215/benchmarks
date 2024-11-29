package main

import "fmt"
import "time"
import "net/http"

func main() {
	var before = time.Now().UnixMilli()

	_, err := http.Get("http://httpbin.org/get")
	if err != nil {
		return
	}

	var after = time.Now().UnixMilli()

	fmt.Println(after - before)
}
