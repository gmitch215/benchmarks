before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

arr = Array.new(1000) { nil }
(0...1000).each do |i|
  arr[i] = i
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before