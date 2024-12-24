<?php

$before = floor(microtime(true) * 1000);

@file_get_contents('http://httpbin.org/get');

$after = floor(microtime(true) * 1000);

echo $after - $before;