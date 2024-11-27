let before = process.hrtime.bigint()

let n = 0;
for (let i = 0; i < 1000000; i++) {
    n = Math.random()
}

let after = process.hrtime.bigint()

console.log((after - before).toString())