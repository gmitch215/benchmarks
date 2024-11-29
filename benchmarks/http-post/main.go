package main

import (
	"fmt"
	"strings"
)
import "time"
import "net/http"

func main() {
	var before = time.Now().UnixMilli()

	var body = strings.NewReader("Hello World")
	_, err := http.Post("http://httpbin.org/post", "text/plain", body)
	if err != nil {
		return
	}

	var after = time.Now().UnixMilli()

	fmt.Println(after - before)
}
