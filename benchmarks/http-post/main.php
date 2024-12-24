<?php

$before = floor(microtime(true) * 1000);

$url = 'http://httpbin.org/post';
$options = [
    'http' => [
        'header' => "Content-Type: text/plain\r\nContent-Length: " . strlen('Hello World'),
        'method' => 'POST',
        'content' => 'Hello World',
    ],
];

$context = stream_context_create($options);

@file_get_contents($url, false, $context);

$after = floor(microtime(true) * 1000);

echo $after - $before;