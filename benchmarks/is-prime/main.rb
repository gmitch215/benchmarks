def is_prime(n)
  start = 2
  limit = Math.sqrt(n)

  while start <= limit
    if n % start == 0
      return false
    end
    start += 1
  end

  return true
end

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

for i in 1..100000
  is_prime(i)
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before