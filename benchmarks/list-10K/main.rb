before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

list = []
(0...10000).each { |i|
  list.push(i)
}

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before