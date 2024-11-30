<?php

$before = hrtime(true);

$n = 0;
for ($i = 0; $i < 1000; $i++) {
    $n = rand();
}

$after = hrtime(true);

echo $after - $before;