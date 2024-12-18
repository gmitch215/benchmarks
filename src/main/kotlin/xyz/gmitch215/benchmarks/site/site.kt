@file:JvmName("SiteCreator")

package xyz.gmitch215.benchmarks.site

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun
import java.io.File

suspend fun main(args: Array<String>): Unit = withContext(Dispatchers.IO) {
    val input = File(args[0])
    val output = File(args[1])

    logger.info { "Started Site Data Creation" }
    logger.debug { "Input: ${input.absolutePath}" }
    logger.debug { "Output: ${output.absolutePath}" }

    if (!output.exists())
        output.mkdirs()

    val data = File(output, "_data")
    if (!data.exists())
        data.mkdirs()

    val results = File(data, "results")
    if (!results.exists() || results.list() == null)
        error("No results found. Run benchmarks (or preview task) first")

    val topFolders = results.listFiles { file -> file.isDirectory } ?: emptyArray()

    logger.info { "Found ${topFolders.size} Platforms" }

    val folders = input.listFiles { file -> file.isDirectory && file.name != "output" } ?: emptyArray()
    logger.debug { "Found ${folders.size} Benchmarks"}

    // benchmarks.yml
    val benchmarks = mutableListOf<BenchmarkConfiguration>()
    val benchmarksData = File(data, "benchmarks.yml")
    if (benchmarksData.exists()) {
        logger.info { "benchmarks.yml already exists" }
        benchmarks.addAll(Yaml.default.decodeFromString(benchmarksData.readText()))
    } else {
        benchmarksData.createNewFile()

        benchmarks.addAll(folders.map { folder ->
            val configFile = File(folder, "config.yml").readText()
            val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)
            config.id = folder.name

            return@map config
        }.sortedBy { it.id })

        benchmarksData.writeText(Yaml.default.encodeToString(benchmarks))
        logger.info { "Created benchmarks.yml" }
        logger.debug { "Wrote ${benchmarksData.absolutePath}" }
    }

    coroutineScope {
        val runs = Yaml.default.decodeFromString<List<BenchmarkRun>>(File(input, "config.yml").readText())

        // stats.yml
        launch {
            val statsData = File(data, "stats.yml")
            if (statsData.exists()) {
                logger.info { "stats.yml already exists" }
                return@launch
            }

            statsData.createNewFile()

            val languagesCount = runs.map { it.id }.distinct().count()

            val stats = Yaml.default.encodeToString(mapOf(
                "benchmarks" to folders.size,
                "languages" to languagesCount,
                "platforms" to topFolders.size
            ))

            statsData.writeText(stats)
            logger.info { "Created stats.yml" }
            logger.debug { "Wrote ${statsData.absolutePath}" }
        }

        // versus.yml
        launch {
            val versusData = File(data, "versus.yml")
            if (versusData.exists()) {
                logger.info { "versus.yml already exists" }
                return@launch
            }

            versusData.createNewFile()

            val pairs0 = mutableListOf<Pair<BenchmarkRun, BenchmarkRun>>()
            for (i in runs.indices)
                for (j in i + 1 until runs.size) {
                    pairs0.add(runs[i] to runs[j])
                }

            // Sort Pairs Alphabetically
            val pairs = mutableListOf<Map<String, BenchmarkRun>>()
            for (match in pairs0)
                pairs.add(mapOf(
                    "l1" to if (match.first.id < match.second.id) match.first else match.second,
                    "l2" to if (match.first.id < match.second.id) match.second else match.first
                ))

            logger.debug { "Found ${pairs.size} pairs for versus" }

            val versus = Yaml.default.encodeToString(
                pairs.sortedBy { map -> "${map["l1"]!!.id}-vs-${map["l2"]!!.id}" }
            )

            versusData.writeText(versus)
            logger.info { "Created versus.yml" }
            logger.debug { "Wrote ${versusData.absolutePath}" }
        }
    }

    logger.info { "Finished Site Data Creation" }

    val outputFiles = data.listFiles() ?: emptyArray()
    if (outputFiles.isEmpty()) {
        logger.error { "No files were created" }
        return@withContext
    }

    logger.debug { "Data Output Files: ${outputFiles.joinToString()}" }

    coroutineScope {
        logger.info { "Writing benchmark info files" }
        logger.debug { "Found ${topFolders.size} Platforms" }

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
                            val benchmarkFolder = File(rootFolder, "versus/${benchmark.id}")
                            if (!benchmarkFolder.exists())
                                benchmarkFolder.mkdirs()

                            launch {
                                val indexFile = File(benchmarkFolder, "index.md")
                                if (!indexFile.exists())
                                    indexFile.createNewFile()

                                indexFile.writeText(VERSUS_FILE_INDEX_TEMPLATE(platform, benchmark))
                                logger.debug { "Created versus index file for ${benchmark.id}" }
                            }

                            for (match in versus)
                                launch {
                                    val l1 = match["l1"] ?: error("l1 not found for ${benchmark.id}")
                                    val l2 = match["l2"] ?: error("l2 not found for ${benchmark.id}")

                                    val benchmarkFile = File(benchmarkFolder, "${l1.id}-vs-${l2.id}.md")
                                    if (!benchmarkFile.exists())
                                        benchmarkFile.createNewFile()

                                    benchmarkFile.writeText(VERSUS_FILE_TEMPLATE(platform, benchmark, l1, l2))
                                    logger.info { "Created ${benchmarkFile.absolutePath}" }
                                }
                        }

                }
            }
    }
}