<?php

function is_prime($n): bool
{
    $start = 2;
    $limit = sqrt($n);

    while ($start <= $limit) {
        if ($n % $start++ < 1) return false;
    }

    return $n > 1;
}

$before = hrtime(true);

for ($i = 0; $i < 100000; $i++) {
    is_prime($i);
}

$after = hrtime(true);

echo $after - $before;