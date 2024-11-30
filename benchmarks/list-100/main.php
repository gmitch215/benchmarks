<?php

$before = hrtime(true);

$arr = [];
for ($i = 0; $i < 100; $i++) {
    $arr[] = $i;
}

$after = hrtime(true);

echo $after - $before;