use std::time::{SystemTime};

const BASE64_CHARS: &[u8; 64] = b"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

fn encode_base64(input: &[u8]) -> String {
    let mut output = String::new();
    let mut buffer = 0u32;
    let mut bits_in_buffer = 0;

    for &byte in input {
        buffer = (buffer << 8) | byte as u32;
        bits_in_buffer += 8;

        while bits_in_buffer >= 6 {
            bits_in_buffer -= 6;
            let index = (buffer >> bits_in_buffer) & 0b111111;
            output.push(BASE64_CHARS[index as usize] as char);
        }
    }

    if bits_in_buffer > 0 {
        buffer <<= 6 - bits_in_buffer;
        let index = buffer & 0b111111;
        output.push(BASE64_CHARS[index as usize] as char);
    }

    while output.len() % 4 != 0 {
        output.push('=');
    }

    output
}

fn main() {
    let i1 = "Hello, world!";
    let i2 = "The quick brown fox jumps over the lazy dog.";
    let i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    encode_base64(i1.as_bytes());
    encode_base64(i2.as_bytes());
    encode_base64(i3.as_bytes());

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}