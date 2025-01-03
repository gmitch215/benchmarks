use std::time::{SystemTime};

fn is_prime(n: u64) -> bool {
    let mut start = 2;
    let limit = (n as f64).sqrt();

    while start <= limit as u64 {
        if n % start == 0 {
            return false;
        }
        start += 1;
    }

    true
}

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    for i in 0..100_000 {
        is_prime(i);
    }

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}