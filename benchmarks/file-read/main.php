<?php

$before = hrtime(true);

$file = fopen('file.txt', 'r');
fread($file, filesize('file.txt'));
fclose($file);

$after = hrtime(true);

echo $after - $before;