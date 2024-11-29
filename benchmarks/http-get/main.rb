require 'net/http'

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

Net::HTTP.get('httpbin.org', '/get')

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

puts after - before