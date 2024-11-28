before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

arr = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834]

arr.bsearch { |x| x == arr[2] }
arr.bsearch { |x| x == arr[8] }
arr.bsearch { |x| x == arr[12] }
arr.bsearch { |x| x == arr[15] }
arr.bsearch { |x| x == arr[20] }

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before