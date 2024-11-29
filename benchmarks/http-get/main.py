import time
import requests

before = round(time.time() * 1000)

requests.get('http://httpbin.org/get')

after = round(time.time() * 1000)

print(after - before)
