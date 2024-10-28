before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

arr = Array.new(100_000) { nil }
(0...100_000).each { |i|
  arr[i] = rand
}

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before