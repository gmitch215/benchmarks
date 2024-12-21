---
layout: versus
benchmark: 3n + 1 Problem
id: 3n-plus-1
platform: linux
l1: c-gnu
l1-display: C (GCC)
l1-file-name: main.c
l2: cpp-llvm
l2-display: C++ (Clang++)
l2-file-name: main.cpp
title: 3n + 1 Problem | C (GCC) vs C++ (Clang++) | Linux
tags: [number, math, algorithm]
disabled: []
comments: true
---

The Collatz Conjecture, also known as 3n + 1, is a mathematical problem that has puzzled mathematicians for years.  The conjecture states that if you take any positive integer n, and if n is even, divide it by 2, and if n is odd, multiply it by 3 and add 1, and repeat this process, you will eventually reach the number 1.  This benchmark tests the performance of the 3n + 1 problem on the number "837,799" which takes 524 steps to reach 1.<br>