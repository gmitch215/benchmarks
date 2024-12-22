<?php

$before = hrtime(true);

$i1 = "Hello, World!";
$i2 = "The quick brown fox jumps over the lazy dog.";
$i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

base64_encode($i1);
base64_encode($i2);
base64_encode($i3);

$after = hrtime(true);

echo $after - $before;