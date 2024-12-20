use std::io::{Write};
use std::net::TcpStream;
use std::time::{SystemTime};

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let host = "httpbin.org";
    let port = 80;

    let mut stream = TcpStream::connect((host, port)).unwrap();

    let request = format!(
        "GET /get HTTP/1.1\r\n\
         Host: {}\r\n\
         Connection: close\r\n\
         \r\n", host);

    stream.write_all(request.as_bytes()).unwrap();

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_millis() - before.as_millis());
}