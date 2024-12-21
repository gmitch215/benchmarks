<?php

function collatz_next($n): int {
    if ($n % 2 == 0) {
        return $n / 2;
    } else {
        return 3 * $n + 1;
    }
}

$before = hrtime(true);

$n = 837799;
while ($n != 1) {
    $n = collatz_next($n);
}

$after = hrtime(true);

echo $after - $before;