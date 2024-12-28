const fs = require('fs')

let before = process.hrtime.bigint()

fs.readFileSync('file.txt', 'utf8')

let after = process.hrtime.bigint()

console.log((after - before).toString())