@file:JvmName("DataCreator")

package xyz.gmitch215.benchmarks.site

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.Path

suspend fun main(args: Array<String>): Unit = coroutineScope {
    val input = File(args[0])
    val zipFiles = File(args[1])
    val output = File(args[2])

    logger.info { "Started Site Data Creation" }
    logger.debug { "Input: ${input.absolutePath}" }
    logger.debug { "Output: ${output.absolutePath}" }

    val folders = input.listFiles { file -> file.isDirectory && file.name != "output" }
    logger.debug { "Found ${folders.size} Benchmarks"}

    // benchmarks.yml
    launch {
        val benchmarksData = File(output, "benchmarks.yml")
        if (benchmarksData.exists()) {
            logger.info { "benchmarks.yml already exists" }
            return@launch
        }

        benchmarksData.createNewFile()

        val configs = Yaml.default.encodeToString(folders.map { folder ->
            val configFile = File(folder, "config.yml").readText()
            val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)
            config.id = folder.name

            return@map config
        }.sortedBy { it.id })

        benchmarksData.writeText(configs)
        logger.info { "Created benchmarks.yml" }
    }

    val runs = Yaml.default.decodeFromString<List<BenchmarkRun>>(File(input, "config.yml").readText())

    // stats.yml
    launch {
        val statsData = File(output, "stats.yml")
        if (statsData.exists()) {
            logger.info { "stats.yml already exists" }
            return@launch
        }

        statsData.createNewFile()

        val languagesCount = runs.map { it.id }.distinct().count()

        val stats = Yaml.default.encodeToString(mapOf<String, Int>(
            "benchmarks" to folders.size,
            "languages" to languagesCount,
            "platforms" to 3
        ))

        statsData.writeText(stats)
        logger.info { "Created stats.yml" }
    }

    // versus.yml
    launch {
        val versusData = File(output, "versus.yml")
        if (versusData.exists()) {
            logger.info { "versus.yml already exists" }
            return@launch
        }

        versusData.createNewFile()

        val pairs0 = mutableListOf<Pair<BenchmarkRun, BenchmarkRun>>()
        for (i in 0 until runs.size) {
            for (j in i + 1 until runs.size) {
                pairs0.add(runs[i] to runs[j])
            }
        }

        // Sort Pairs Alphabetically
        val pairs = mutableListOf<Map<String, BenchmarkRun>>()
        for (match in pairs0) {
            val l1: BenchmarkRun; val l2: BenchmarkRun
            if (match.first.id < match.second.id) {
                l1 = match.first
                l2 = match.second
            } else {
                l1 = match.second
                l2 = match.first
            }

            pairs.add(mapOf<String, BenchmarkRun>(
                "l1" to l1,
                "l2" to l2
            ))
        }

        logger.debug { "Found ${pairs.size} pairs for versus" }

        val versus = Yaml.default.encodeToString(
            pairs.sortedBy { map -> "${map["l1"]!!.id}-vs-${map["l2"]!!.id}" }
        )

        versusData.writeText(versus)
        logger.info { "Created versus.yml" }
    }

    logger.info { "Unzipping Benchmarks..." }
    val unzipped = unzipBenchmarks(zipFiles)
    logger.info { "Unzipped Benchmarks" }

    launch {
        for (file in unzipped)
            launch {
                val dataOut = File(output, "results/${file.name}")
                if (dataOut.exists())
                    dataOut.deleteRecursively()

                if (!file.renameTo(dataOut))
                    file.copyRecursively(dataOut, true)

                logger.debug { "Copied ${file.name} to ${dataOut.absolutePath}" }
            }
    }.join()

    logger.info { "Finished Site Data Creation" }
}

suspend fun unzipBenchmarks(folder: File): List<File> = withContext(Dispatchers.IO) {
    val files = folder.listFiles { file -> file.extension == "zip" }

    val unzipped = mutableListOf<File>()
    val mutex = Mutex()

    launch {
        files.forEach { file ->
            launch {
                val os = file.nameWithoutExtension.substringAfter('-')
                val dest = File(folder, os)

                if (dest.exists())
                    dest.deleteRecursively()

                if (!dest.exists())
                    dest.mkdirs()

                logger.debug { "Unzipping ${file.name} to ${dest.absolutePath}" }
                unzip(file.absolutePath, dest.absolutePath)

                mutex.withLock {
                    unzipped.add(dest)
                }
            }
        }
    }.join()

    logger.debug { "Unzipped ${unzipped.size} Files" }
    return@withContext unzipped
}

private fun unzip(zipFile: String, destFolder: String) {
    ZipInputStream(FileInputStream(zipFile)).use { zis ->
        var entry: ZipEntry?
        val buffer = ByteArray(1024)
        while (zis.nextEntry.also { entry = it } != null) {
            val newFile = File(destFolder + File.separator + Path(entry!!.name).normalize())
            if (entry.isDirectory)
                newFile.mkdirs()
            else {
                newFile.parentFile.mkdirs()
                FileOutputStream(newFile).use { fos ->
                    var length: Int
                    while (zis.read(buffer).also { length = it } > 0) {
                        fos.write(buffer, 0, length)
                    }
                }
            }
        }
    }
}