let before = process.hrtime.bigint()

let n = 0;
for (let i = 0; i < 30; i++) {
    n = 1 << i;
}

let after = process.hrtime.bigint()

console.log((after - before).toString())