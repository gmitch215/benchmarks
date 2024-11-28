@file:JvmName("PagesCreator")

package xyz.gmitch215.benchmarks.site

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import java.io.File

val INDEX_FILE_TEMPLATE: (String) -> String = { platform ->
    """
    ---
    layout: platform
    title: $platform
    ---
    """.trimIndent()
}

val INFO_FILE_TEMPLATE: (BenchmarkConfiguration) -> String = { config ->
    """
    ---
    layout: benchmark
    id: ${config.id}
    title: ${config.name}
    summary: ${config.description}
    tags: [${config.tags.joinToString()}]
    ---
    """.trimIndent()
}

suspend fun main(args: Array<String>) = withContext(Dispatchers.IO) {
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

    launch {
        for (folder in topFolders)
            launch {
                val rootFolder = File(output, folder.name)
                rootFolder.mkdirs()

                logger.debug { "Creating index page for ${folder.absolutePath}" }
                launch {
                    val indexFile = File(rootFolder, "index.md")
                    if (!indexFile.exists())
                        indexFile.createNewFile()

                    indexFile.writeText(INDEX_FILE_TEMPLATE(folder.name))
                    logger.info { "Created ${indexFile.absolutePath}" }
                }

                logger.debug { "Creating info files for ${folder.absolutePath}" }
                for (benchmark in benchmarks)
                    launch {
                        val benchmarkFile = File(rootFolder, "${benchmark.id}.md")
                        if (!benchmarkFile.exists())
                            benchmarkFile.createNewFile()

                        benchmarkFile.writeText(INFO_FILE_TEMPLATE(benchmark))
                        logger.info { "Created ${benchmarkFile.absolutePath}" }
                    }
            }
    }.join()

    logger.info { "Finished writing benchmark info files" }
}