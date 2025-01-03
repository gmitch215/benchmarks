function isPrime(n) {
    let start = 2;
    const limit = Math.sqrt(n);

    while (start <= limit) {
        if (n % start++ < 1) return false;
    }

    return n > 1;
}

let before = process.hrtime.bigint()

for (let i = 1; i <= 100_000; i++) {
    isPrime(i)
}

let after = process.hrtime.bigint()

console.log((after - before).toString())