before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

1000.times do
  rand
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before