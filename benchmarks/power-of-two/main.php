<?php

$before = hrtime(true);

$n = 0;
for ($i = 0; $i < 31; $i++) {
    $n = 1 << $i;
}

$after = hrtime(true);

echo $after - $before;