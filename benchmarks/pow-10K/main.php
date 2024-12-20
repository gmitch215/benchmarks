<?php

$before = hrtime(true);

for ($i = 0; $i < 10000; $i++) {
    $y = 1.1 + ($i / 10000.0);
    pow(2, $y);
}

$after = hrtime(true);

echo $after - $before;