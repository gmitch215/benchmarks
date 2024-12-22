import time
import base64

i1 = "SGVsbG8sIFdvcmxkIQ=="
i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4="
i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ=="

before = time.time_ns()

base64.b64decode(i1)
base64.b64decode(i2)
base64.b64decode(i3)

after = time.time_ns()

print(after - before)
