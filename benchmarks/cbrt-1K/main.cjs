let before = process.hrtime.bigint()

for (let i = 0; i < 1000; i++) {
    Math.cbrt(i)
}

let after = process.hrtime.bigint()

console.log((after - before).toString())