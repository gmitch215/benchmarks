require 'net/http'

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

begin
  Net::HTTP.get('httpbin.org', '/get')
rescue Exception
  false
end
after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

puts after - before