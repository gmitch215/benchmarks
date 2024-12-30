extern crate base64;

use std::time::{SystemTime};
use base64::prelude::*;

fn main() {
    let i1 = "SGVsbG8sIFdvcmxkIQ==";
    let i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    let i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let _ = BASE64_STANDARD.decode(i1);
    let _ = BASE64_STANDARD.decode(i2);
    let _ = BASE64_STANDARD.decode(i3);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}