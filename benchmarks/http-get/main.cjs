const http = require('http')

let before = Date.now()

http.get('http://httpbin.org/get')

let after = Date.now()

console.log((after - before).toString())