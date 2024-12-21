before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

def collatz_next(n)
  if n % 2 == 0
    n / 2
  else
    3 * n + 1
  end
end

n = 837799
while n != 1
  n = collatz_next(n)
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before