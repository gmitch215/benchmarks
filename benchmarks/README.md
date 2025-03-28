# Benchmark Information

This directory contains the benchmarks used in the repository. The benchmarks are written to perform various operations and record the time taken between them.

Compiler optimization should be disabled by default, if possible, to ensure that the benchmarks are not optimized out. 
If not possible, they should be lowered to the minimum level possible.

The folders in this directory represent the benchmark name, and each folder contains a `main.*` file for the language's benchmarks.
They are ran a number of times in parallel, will all the times collected.

The [`config.yml`](config.yml) file contains the configuration for the benchmarks, which can include compiling and/or running a benchmark.

## Dependency Information

This folder also contains various dependency files for each programming language. These files are used to install the dependencies for the benchmarks.

These usually only apply to languages that don't have a `compile` step in the benchmarks. If you are looking for libraries, look in the [`lib`](../lib) directory.