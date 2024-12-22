let before = process.hrtime.bigint()

const i1 = "Hello, World!"
const i2 = "The quick brown fox jumps over the lazy dog."
const i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-"

Buffer.from(i1).toString('base64')
Buffer.from(i2).toString('base64')
Buffer.from(i3).toString('base64')

let after = process.hrtime.bigint()

console.log((after - before).toString())