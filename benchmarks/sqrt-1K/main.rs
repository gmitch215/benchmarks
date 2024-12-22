use std::time::{SystemTime};

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    for i in 0..1000 {
        let _ = (i as f64).sqrt();
    }

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}