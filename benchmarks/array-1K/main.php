<?php

$before = hrtime(true);

$arr = array_fill(0, 1000, 0);
for ($i = 0; $i < 1000; $i++) {
    $arr[$i] = $i;
}

$after = hrtime(true);

echo $after - $before;