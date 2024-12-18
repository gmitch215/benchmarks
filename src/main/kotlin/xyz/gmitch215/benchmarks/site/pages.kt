@file:JvmName("PagesCreator")

package xyz.gmitch215.benchmarks.site

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun
import java.io.File

val INDEX_FILE_TEMPLATE: (String) -> String = { platform ->
    """
    ---
    layout: platform
    platform: $platform
    title: ${platform.replaceFirstChar { it.uppercase() }}
    type: Benchmarks
    suburl: /
    ---
    """.trimIndent()
}

val VERSUS_INDEX_TEMPLATE: (String) -> String = { platform ->
    """
    ---
    layout: platform
    platform: $platform
    title: Versus - ${platform.replaceFirstChar { it.uppercase() }}
    type: Versus
    suburl: /versus/
    ---
    """.trimIndent()
}

val INFO_FILE_TEMPLATE: (String, BenchmarkConfiguration) -> String = { platform, config ->
    """
    ---
    layout: benchmark
    platform: $platform
    id: ${config.id}
    display: ${config.name}
    title: ${config.name} | ${platform.replaceFirstChar { it.uppercase() }}
    summary: ${config.description}
    tags: [${config.tags.joinToString()}]
    comments: true
    ---
    """.trimIndent()
}

val VERSUS_FILE_INDEX_TEMPLATE: (String, BenchmarkConfiguration) -> String = { platform, config ->
    """
    ---
    layout: versus
    benchmark: ${config.name}
    id: ${config.id}
    platform: $platform
    title: ${config.name} | Select Language | ${platform.replaceFirstChar { it.uppercase() }}
    summary: ${config.description}
    tags: [${config.tags.joinToString()}]
    ---
    """.trimIndent()
}

val VERSUS_FILE_TEMPLATE: (String, BenchmarkConfiguration, BenchmarkRun, BenchmarkRun) -> String = { platform, config, l1, l2 ->
    """
    ---
    layout: versus
    benchmark: ${config.name}
    id: ${config.id}
    platform: $platform
    l1: ${l1.id}
    l1-display: ${l1.language}
    l2: ${l2.id}
    l2-display: ${l2.language}
    title: ${config.name} | ${l1.language} vs ${l2.language} | ${platform.replaceFirstChar { it.uppercase() }}
    summary: ${config.description}
    tags: [${config.tags.joinToString()}]
    comments: true
    ---
    """.trimIndent()
}