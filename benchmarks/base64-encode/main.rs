extern crate base64;

use std::time::{SystemTime};
use base64::prelude::*;

fn main() {
    let i1 = "Hello, world!";
    let i2 = "The quick brown fox jumps over the lazy dog.";
    let i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let _ = BASE64_STANDARD.encode(i1);
    let _ = BASE64_STANDARD.encode(i2);
    let _ = BASE64_STANDARD.encode(i3);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}