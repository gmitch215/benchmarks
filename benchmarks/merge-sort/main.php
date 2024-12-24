<?php

function merge(&$arr, $l, $m, $r): void {
    $n1 = $m - $l + 1;
    $n2 = $r - $m;

    $L = array();
    $R = array();

    for ($i = 0; $i < $n1; $i++)
        $L[$i] = $arr[$l + $i];

    for ($j = 0; $j < $n2; $j++)
        $R[$j] = $arr[$m + 1 + $j];

    $i = 0;
    $j = 0;
    $k = $l;
    while ($i < $n1 && $j < $n2) {
        if ($L[$i] <= $R[$j]) {
            $arr[$k] = $L[$i];
            $i++;
        } else {
            $arr[$k] = $R[$j];
            $j++;
        }
        $k++;
    }


    while ($i < $n1) {
        $arr[$k] = $L[$i];
        $i++;
        $k++;
    }

    while ($j < $n2) {
        $arr[$k] = $R[$j];
        $j++;
        $k++;
    }
}

function mergeSort(&$arr, $l, $r): void {
    if ($l < $r) {
        $m = $l + (int)(($r - $l) / 2);

        mergeSort($arr, $l, $m);
        mergeSort($arr, $m + 1, $r);
        merge($arr, $l, $m, $r);
    }
}

$before = hrtime(true);

$arr = [312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984];
mergeSort($arr, 0, count($arr) - 1);

$after = hrtime(true);

echo $after - $before;