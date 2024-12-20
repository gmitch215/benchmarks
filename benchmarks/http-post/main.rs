use std::io::{Write};
use std::net::TcpStream;
use std::time::{SystemTime};

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let host = "httpbin.org";
    let port = 80;
    let data = "Hello World";

    let request = format!(
        "POST /post HTTP/1.1\r\n\
         Host: {}\r\n\
         Content-Type: text/plain\r\n\
         Content-Length: {}\r\n\
         Connection: close\r\n\
         \r\n\
         {}", host, data.len(), data
    );

    let mut stream = TcpStream::connect((host, port)).expect("Failed to connect to server");

    stream.write_all(request.as_bytes()).expect("Failed to send request");

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_millis() - before.as_millis());
}