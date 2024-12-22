include Math

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

for i in 0..999 do
  Math.cbrt(i)
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before