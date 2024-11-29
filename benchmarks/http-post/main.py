import time
import requests

before = round(time.time() * 1000)

headers = {
    'Content-Type': 'text/plain',
    'Content-Length': '11'
}

requests.post('http://httpbin.org/post', data="Hello World", headers=headers)

after = round(time.time() * 1000)

print(after - before)
