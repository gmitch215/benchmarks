import time
import base64

i1 = "Hello, World!"
i2 = "The quick brown fox jumps over the lazy dog."
i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-"

before = time.time_ns()

base64.b64encode(i1.encode())
base64.b64encode(i2.encode())
base64.b64encode(i3.encode())

after = time.time_ns()

print(after - before)
