<?php

$i1 = "SGVsbG8sIFdvcmxkIQ==";
$i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
$i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

$before = hrtime(true);

base64_decode($i1);
base64_decode($i2);
base64_decode($i3);

$after = hrtime(true);

echo $after - $before;