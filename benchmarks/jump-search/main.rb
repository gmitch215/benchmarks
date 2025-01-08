def jump_search array, item
  n = array.size
  i = 0
  m = Math.sqrt n
  
  while array[m] <= item && m < n do  
    i = m
    m += Math.sqrt n
    return -1 if m > n - 1
  end
    
  start = i
  array.each_with_index do |el, idx|
    return idx if el == item
  end
    
  nil
end

arr = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834]

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

jump_search arr, arr[2]
jump_search arr, arr[8]
jump_search arr, arr[12]
jump_search arr, arr[15]
jump_search arr, arr[20]

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before