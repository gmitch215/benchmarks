<?php

$before = hrtime(true);

for ($i = 0; $i < 1000; $i++) {
    sqrt($i);
}

$after = hrtime(true);

echo $after - $before;