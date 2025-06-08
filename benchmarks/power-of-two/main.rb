before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

n = 0
(0..30).each { |i|
  n = 1 << i
}

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before