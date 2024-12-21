---
layout: versus
benchmark: HTTP POST Request
id: http-post
platform: linux
l1: c-llvm
l1-display: C (Clang)
l1-file-name: main.c
l2: rust
l2-display: Rust
l2-file-name: main.rs
title: HTTP POST Request | C (Clang) vs Rust | Linux
tags: [web, request]
disabled: [kotlin-native]
comments: true
---

Performs an HTTP POST Request to a specified URL with "Hello World" as the request body.