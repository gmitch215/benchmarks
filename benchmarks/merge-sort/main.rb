def merge(left, right)
  if right.empty?
    return left
  end

  if left.empty?
    return right
  end

  min = if left.first <= right.first
          left.shift
        else
          right.shift
        end

  rec = merge(left, right)
  [min].concat(rec)
end

def merge_sort(arr)
  num = arr.length
  if num <= 1
    return arr
  end

  half = (num / 2).round

  left  = arr.take(half)
  right = arr.drop(half)

  sleft = merge_sort(left)
  sright = merge_sort(right)
  merge(sleft, sright)
end

before = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

arr = [312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984]
merge_sort(arr)

after = Process.clock_gettime(Process::CLOCK_MONOTONIC, :nanosecond)

puts after - before