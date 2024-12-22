package main

import "fmt"
import "time"
import "encoding/base64"

func main() {
	var i1 = "SGVsbG8sIFdvcmxkIQ=="
	var i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4="
	var i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ=="

	var before = time.Now().UnixNano()

	_, _ = base64.StdEncoding.DecodeString(i1)
	_, _ = base64.StdEncoding.DecodeString(i2)
	_, _ = base64.StdEncoding.DecodeString(i3)

	var after = time.Now().UnixNano()

	fmt.Println(after - before)
}
