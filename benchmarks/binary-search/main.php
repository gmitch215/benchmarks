<?php

function binary_search($arr, $x) {
    $low = 0;
    $high = count($arr) - 1;

    while ($low <= $high) {
        $mid = $low + intdiv($high - $low, 2);
        if ($arr[$mid] == $x) {
            return $mid;
        }
        if ($arr[$mid] < $x) {
            $low = $mid + 1;
        } else {
            $high = $mid - 1;
        }
    }

    return -1;
}

$before = hrtime(true);

$arr = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834];

binary_search($arr, $arr[2]);
binary_search($arr, $arr[8]);
binary_search($arr, $arr[12]);
binary_search($arr, $arr[15]);
binary_search($arr, $arr[20]);

$after = hrtime(true);

echo $after - $before;