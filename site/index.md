---
layout: index
summary: "Programming benchmarks across different languages and platforms."
---

# benchmarks

{% assign data_count = site.data.stats.benchmarks | times: site.data.stats.languages | times: site.data.stats.platforms | times: 25 | numformat %}

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/gmitch215/benchmarks/build.yml)](https://github.com/gmitch215/benchmarks)
[![GitHub License](https://img.shields.io/github/license/gmitch215/benchmarks)](https://github.com/gmitch215/benchmarks)
[![GitHub Discussions](https://img.shields.io/github/discussions/gmitch215/benchmarks)](https://github.com/gmitch215/benchmarks/discussions)
[![GitHub Repo stars](https://img.shields.io/github/stars/gmitch215/benchmarks?style=flat)](https://github.com/gmitch215/benchmarks)
![Data Count](https://img.shields.io/badge/data_count-{{ data_count | url_encode }}_runs-orange)


Providing **{{ site.data.stats.benchmarks | numformat }}** benchmarks across **{{ site.data.stats.languages | numformat }}** languages on **{{ site.data.stats.platforms | numformat }}** platforms.

This is a collection of benchmarks across different languages and platforms. The goal is to provide a consistent set of benchmarks to compare the performance of different languages and platforms. The benchmarks are intended to be simple and easy to understand, focusing on performance and efficiency. However, not all benchmarks are guarenteed to be available for each language.

Get started with your operating system from above.

*By using this website, you agree to the repository's [license](https://github.com/gmitch215/benchmarks/blob/master/LICENSE).*
