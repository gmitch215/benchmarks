use std::time::{SystemTime};

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    for i in 0..100000 {
        let _ = (i as f64).cbrt();
    }

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}