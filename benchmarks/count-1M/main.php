<?php

$before = hrtime(true);

$count = 0;
for ($i = 0; $i < 1000000; $i++) {
    $count++;
}

$after = hrtime(true);

echo $after - $before;