let before = process.hrtime.bigint()

let arr = Array(1_000)
for (let i = 0; i < 1_000; i++) {
    arr[i] = i
}

let after = process.hrtime.bigint()

console.log((after - before).toString())