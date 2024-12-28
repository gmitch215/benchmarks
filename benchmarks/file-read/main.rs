use std::time::{SystemTime};
use std::io::Read;

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let mut file = std::fs::File::open("file.txt").unwrap();
    let mut contents = String::new();
    file.read_to_string(&mut contents).unwrap();

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}