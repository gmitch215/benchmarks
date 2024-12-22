let before = process.hrtime.bigint()

for (let i = 0; i < 100000; i++) {
    Math.sqrt(i)
}

let after = process.hrtime.bigint()

console.log((after - before).toString())