let before = process.hrtime.bigint()

let count = 0;
for (let i = 0; i < 100_000_000; i++) {
    count++;
}

let after = process.hrtime.bigint()

console.log((after - before).toString())