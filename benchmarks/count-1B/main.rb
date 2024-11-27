before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

count = 0
1_000_000_000.times do
  count += 1
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before