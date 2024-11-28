let before = process.hrtime.bigint()

let arr = Array(100_000)
for (let i = 0; i < 100_000; i++) {
    arr[i] = i
}

let after = process.hrtime.bigint()

console.log((after - before).toString())