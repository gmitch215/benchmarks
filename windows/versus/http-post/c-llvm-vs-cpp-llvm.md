---
layout: versus
benchmark: HTTP POST Request
id: http-post
platform: windows
l1: c-llvm
l1-display: C (Clang)
l1-file-name: main.c
l2: cpp-llvm
l2-display: C++ (Clang++)
l2-file-name: main.cpp
title: HTTP POST Request | C (Clang) vs C++ (Clang++) | Windows
tags: [web, request]
disabled: [kotlin-native]
comments: true
---

Performs an HTTP POST Request to a specified URL with "Hello World" as the request body.