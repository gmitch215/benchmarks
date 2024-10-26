let before = process.hrtime.bigint()

let arr = []
for (let i = 0; i < 100; i++) {
    arr.push(i)
}

let after = process.hrtime.bigint()

console.log((after - before).toString())