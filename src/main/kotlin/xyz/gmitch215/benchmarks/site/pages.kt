@file:JvmName("PagesCreator")

package xyz.gmitch215.benchmarks.site

import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun

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

val ABOUT_PLATFORM_TEMPLATE: (String) -> String = { platform ->
    """
    ---
    layout: platform-page
    platform: $platform
    title: About | ${platform.replaceFirstChar { it.uppercase() }}
    type: About
    suburl: /about/
    ---
    """.trimIndent()
}

val RANKINGS_PLATFORM_TEMPLATE: (String) -> String = { platform ->
    """
    ---
    layout: platform-page
    platform: $platform
    title: Rankings | ${platform.replaceFirstChar { it.uppercase() }}
    type: Rankings
    suburl: /rankings/
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
    tags: [${config.tags.joinToString()}]
    comments: true
    ---
    
    ${config.description.replace("\n", "<br>")}
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
    tags: [${config.tags.joinToString()}]
    disabled: [${config.disabled.joinToString()}]
    ---
    
    ${config.description.replace("\n", "<br>")}
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
    l1-file-name: ${l1.fileName}
    l2: ${l2.id}
    l2-display: ${l2.language}
    l2-file-name: ${l2.fileName}
    title: ${config.name} | ${l1.language} vs ${l2.language} | ${platform.replaceFirstChar { it.uppercase() }}
    tags: [${config.tags.joinToString()}]
    disabled: [${config.disabled.joinToString()}]
    comments: true
    ---
    
    ${config.description.replace("\n", "<br>")}
    """.trimIndent()
}