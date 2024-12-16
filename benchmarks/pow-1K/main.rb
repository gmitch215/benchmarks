include Math

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

for i in 0..999 do
  y = 1.1 + (i / 1000.0)
  2 ** y
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before