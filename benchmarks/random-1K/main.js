let before = Date.now()

let n = 0;
for (let i = 0; i < 1000; i++) {
    n = Math.random()
}

let after = Date.now()

console.log((after - before).toString())