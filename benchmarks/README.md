# Benchmark Information

This directory contains the benchmarks used in the repository. The benchmarks are written to perform various operations and record the time taken between them.

The folders in this directory represent the benchmark name, and each folder contains a `main.*` file for the language's benchmarks.
They are ran a number of times in parallel, will all the times collected.

The [`config.yml`](config.yml) file contains the configuration for the benchmarks, which can include compiling and/or running a benchmark.

## Dependency Information

This folder also contains various dependency files for each programming language. These files are used to install the dependencies for the benchmarks.