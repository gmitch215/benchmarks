---
layout: versus
benchmark: 3n + 1 Problem
id: 3n-plus-1
platform: linux
l1: kotlin-native
l1-display: Kotlin (Native)
l1-file-name: main.kt
l2: rust
l2-display: Rust
l2-file-name: main.rs
title: 3n + 1 Problem | Kotlin (Native) vs Rust | Linux
tags: [number, math, algorithm]
comments: true
---

The Collatz Conjecture, also known as 3n + 1, is a mathematical problem that has puzzled mathematicians for years.  The conjecture states that if you take any positive integer n, and if n is even, divide it by 2, and if n is odd, multiply it by 3 and add 1, and repeat this process, you will eventually reach the number 1.  This benchmark tests the performance of the 3n + 1 problem on the number "837,799" which takes 524 steps to reach 1.<br>