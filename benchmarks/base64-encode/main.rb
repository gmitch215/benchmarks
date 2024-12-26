require 'base64'

i1 = "Hello, World!"
i2 = "The quick brown fox jumps over the lazy dog."
i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-"

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

Base64.encode64(i1)
Base64.encode64(i2)
Base64.encode64(i3)

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before