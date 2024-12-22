use std::time::{SystemTime};

const BASE64_CHARS: &[u8; 64] = b"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

fn decode_base64(input: &str) -> Result<Vec<u8>, String> {
    let mut output = Vec::new();
    let mut buffer = 0u32;
    let mut bits_in_buffer = 0;

    let mut decode_table = [None; 256];
    for (i, &ch) in BASE64_CHARS.iter().enumerate() {
        decode_table[ch as usize] = Some(i as u8);
    }

    for &byte in input.as_bytes() {
        if byte == b'=' {
            break;
        }

        let value = match decode_table[byte as usize] {
            Some(v) => v as u32,
            None => return Err(format!("Invalid Base64 character: {}", byte as char)),
        };

        buffer = (buffer << 6) | value;
        bits_in_buffer += 6;

        while bits_in_buffer >= 8 {
            bits_in_buffer -= 8;
            output.push((buffer >> bits_in_buffer) as u8);
            buffer &= (1 << bits_in_buffer) - 1;
        }
    }

    Ok(output)
}

fn main() {
    let i1 = "SGVsbG8sIFdvcmxkIQ==";
    let i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    let i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let _ = decode_base64(i1);
    let _ = decode_base64(i2);
    let _ = decode_base64(i3);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}