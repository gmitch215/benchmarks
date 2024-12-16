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

suspend fun main(args: Array<String>): Unit = withContext(Dispatchers.IO) {
    val data = File(args[0])
    val output = File(args[1])

    logger.info { "Writing benchmark info files" }

    val benchmarksFile = File(data, "benchmarks.yml")
    if (!benchmarksFile.exists())
        error("Benchmarks data file does not exist")

    val benchmarks = Yaml.default.decodeFromString<List<BenchmarkConfiguration>>(benchmarksFile.readText())

    logger.debug { "Found ${benchmarks.size} benchmarks" }

    val results = File(data, "results")
    val topFolders = results.listFiles { file -> file.isDirectory }

    logger.debug { "Found ${topFolders.size} platforms" }

    for (folder in topFolders)
        launch {
            val platform = folder.name
            val rootFolder = File(output, platform)
            rootFolder.mkdirs()

            logger.debug { "Creating index page for ${folder.absolutePath}" }
            launch {
                val indexFile = File(rootFolder, "index.md")
                if (!indexFile.exists())
                    indexFile.createNewFile()

                indexFile.writeText(INDEX_FILE_TEMPLATE(platform))
                logger.info { "Created ${indexFile.absolutePath}" }
            }

            // Benchmark Pages
            launch {
                logger.debug { "Creating info files for ${folder.absolutePath}" }
                for (benchmark in benchmarks)
                    launch {
                        val benchmarkFile = File(rootFolder, "${benchmark.id}.md")
                        if (!benchmarkFile.exists())
                            benchmarkFile.createNewFile()

                        benchmarkFile.writeText(INFO_FILE_TEMPLATE(platform, benchmark))
                        logger.info { "Created ${benchmarkFile.absolutePath}" }
                    }
            }

            // Versus Pages
            launch {
                logger.debug { "Creating versus files for ${folder.absolutePath}" }

                val versusFile = File(data, "versus.yml")
                if (!versusFile.exists())
                    error("Versus data file does not exist")

                val versus = Yaml.default.decodeFromString<List<Map<String, BenchmarkRun>>>(versusFile.readText())

                val versusFolder = File(rootFolder, "versus")
                if (!versusFolder.exists())
                    versusFolder.mkdirs()

                launch {
                    val indexFile = File(versusFolder, "index.md")
                    if (!indexFile.exists())
                        indexFile.createNewFile()

                    indexFile.writeText(VERSUS_INDEX_TEMPLATE(platform))
                    logger.debug { "Created index file for versus" }
                }

                for (benchmark in benchmarks)
                    launch {
                        val folder = File(rootFolder, "versus/${benchmark.id}")
                        if (!folder.exists())
                            folder.mkdirs()

                        launch {
                            val indexFile = File(folder, "index.md")
                            if (!indexFile.exists())
                                indexFile.createNewFile()

                            indexFile.writeText(VERSUS_FILE_INDEX_TEMPLATE(platform, benchmark))
                            logger.debug { "Created versus index file for ${benchmark.id}" }
                        }

                        for (match in versus)
                            launch {
                                val l1 = match["l1"] ?: error("l1 not found")
                                val l2 = match["l2"] ?: error("l2 not found")

                                val versusFile = File(folder, "${l1.id}-vs-${l2.id}.md")
                                if (!versusFile.exists())
                                    versusFile.createNewFile()

                                versusFile.writeText(VERSUS_FILE_TEMPLATE(platform, benchmark, l1, l2))
                                logger.info { "Created ${versusFile.absolutePath}" }
                            }
                    }

            }
        }
}