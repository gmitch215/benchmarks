const http = require('http')

let before = Date.now()

const options = {
    host: 'httpbin.org',
    port: '80',
    path: '/post',
    method: 'POST',
    headers: {
        'Content-Type': 'text/plain',
        'Content-Length': 11
    },
    body: 'Hello World'
};

const req = http.request(options)
req.write(options.body)
req.end()

let after = Date.now()

console.log((after - before).toString())