<?php

$before = hrtime(true);

for ($i = 0; $i < 100000; $i++) {
    $i ** (1/3);
}

$after = hrtime(true);

echo $after - $before;