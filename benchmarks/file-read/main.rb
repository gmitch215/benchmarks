before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

File.read("file.txt")

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before