package main

import "fmt"
import "time"
import "encoding/base64"

func main() {
	var i1 = "Hello, World!"
	var i2 = "The quick brown fox jumps over the lazy dog."
	var i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-"

	var before = time.Now().UnixNano()

	base64.StdEncoding.EncodeToString([]byte(i1))
	base64.StdEncoding.EncodeToString([]byte(i2))
	base64.StdEncoding.EncodeToString([]byte(i3))

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
