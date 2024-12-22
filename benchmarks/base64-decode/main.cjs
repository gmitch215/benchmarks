const i1 = "SGVsbG8sIFdvcmxkIQ==";
const i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
const i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

let before = process.hrtime.bigint()

Buffer.from(i1).toString('base64')
Buffer.from(i2).toString('base64')
Buffer.from(i3).toString('base64')

let after = process.hrtime.bigint()

console.log((after - before).toString())