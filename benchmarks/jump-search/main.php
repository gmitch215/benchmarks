<?php

function jump_search($arr, $x) {
    $n = count($arr);
    $step = sqrt($n);
    $prev = 0;
    while ($arr[min($step, $n)-1] < $x) {
        $prev = $step;
        $step += sqrt($n);
        if ($prev >= $n)
            return -1;
    }
 
    while ($arr[$prev] < $x) {
        $prev++;
        if ($prev == min($step, $n))
            return -1;
    }
    
    if ($arr[$prev] == $x)
        return $prev;
 
    return -1;
}

$arr = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834];

$before = hrtime(true);

jump_search($arr, $arr[2]);
jump_search($arr, $arr[8]);
jump_search($arr, $arr[12]);
jump_search($arr, $arr[15]);
jump_search($arr, $arr[20]);

$after = hrtime(true);

echo $after - $before;