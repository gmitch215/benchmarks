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
import kotlinx.serialization.json.JsonObject
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.BenchmarkConfiguration
import xyz.gmitch215.benchmarks.measurement.BenchmarkRun
import xyz.gmitch215.benchmarks.measurement.json
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

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
        if (!benchmarksData.exists())
            benchmarksData.createNewFile()

        val configs = Yaml.default.encodeToString(folders.map { folder ->
            val configFile = File(folder, "config.yml").readText()
            val config = Yaml.default.decodeFromString<BenchmarkConfiguration>(configFile)
            config.id = folder.name

            return@map config
        })

        benchmarksData.writeText(configs)
        logger.info { "Created benchmarks.yml" }
    }

    // stats.yml
    launch {
        val statsData = File(output, "stats.yml")
        if (!statsData.exists())
            statsData.createNewFile()

        val languagesCount = Yaml.default.decodeFromString<List<BenchmarkRun>>(
            File(input, "config.yml").readText()
        ).map { it.id }.distinct().count()

        val stats = Yaml.default.encodeToString(mapOf<String, Int>(
            "benchmarks" to folders.size,
            "languages" to languagesCount,
            "platforms" to 3
        ))

        statsData.writeText(stats)
        logger.info { "Created stats.yml" }
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
            }
    }.join()

    logger.info { "Finished Site Data Creation" }
}

suspend fun unzipBenchmarks(folder: File): List<File> = coroutineScope {
    val files = folder.listFiles { file -> file.extension == "zip" }

    val unzipped = mutableListOf<File>()
    val mutex = Mutex()

    launch {
        launch {
            files.forEach { file ->
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

    return@coroutineScope unzipped
}

private fun unzip(zipFile: String, destFolder: String) {
    ZipInputStream(FileInputStream(zipFile)).use { zis ->
        var entry: ZipEntry?
        val buffer = ByteArray(1024)
        while (zis.nextEntry.also { entry = it } != null) {
            val newFile = File(destFolder + File.separator + entry!!.name)
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