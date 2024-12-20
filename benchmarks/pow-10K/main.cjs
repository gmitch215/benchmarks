let before = process.hrtime.bigint()

for (let i = 0; i < 10000; i++) {
    const y = 1.1 + (i / 10000)
    Math.pow(2, y)
}

let after = process.hrtime.bigint()

console.log((after - before).toString())