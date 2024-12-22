require 'base64'

i1 = "SGVsbG8sIFdvcmxkIQ=="
i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4="
i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ=="

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

Base64.decode64(i1)
Base64.decode64(i2)
Base64.decode64(i3)

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before