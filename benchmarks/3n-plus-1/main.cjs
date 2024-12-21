function next(n) {
    if (n % 2 === 0) {
        return n / 2;
    } else {
        return (3 * n) + 1;
    }
}

let before = process.hrtime.bigint()

let n = 837799;
while (n !== 1) {
    n = next(n);
}

let after = process.hrtime.bigint()

console.log((after - before).toString())