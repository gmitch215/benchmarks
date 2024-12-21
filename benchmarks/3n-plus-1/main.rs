use std::time::{SystemTime};

fn next(n: u64) -> u64 {
    if n % 2 == 0 {
        n / 2
    } else {
        3 * n + 1
    }
}

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let mut n = 837799;
    while n != 1 {
        n = next(n);
    }

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}