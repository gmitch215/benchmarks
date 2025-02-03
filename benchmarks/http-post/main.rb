require 'net/http'

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

uri = URI.parse('http://httpbin.org/post')

request = Net::HTTP::Post.new(uri)
request['Content-Type'] = 'text/plain'
request.body = 'Hello World'

begin
  Net::HTTP.start(uri.host, uri.port) do |http|
    http.request(request)
  end
rescue Exception
  false
end

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :millisecond)

puts after - before