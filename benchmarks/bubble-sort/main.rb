def bubble_sort(a)
  n = a.length
  (0...n - 1).each { |i|
    swapped = false
    (0...n - i - 1).each { |j|
      if a[j] > a[j + 1]
        temp = a[j]
        a[j] = a[j + 1]
        a[j + 1] = temp
        swapped = true
      end
    }
    unless swapped
      break
    end
  }
  a
end

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

arr = [639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966]
bubble_sort(arr)

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before