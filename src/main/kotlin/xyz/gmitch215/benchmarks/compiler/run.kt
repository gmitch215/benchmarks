@file:JvmName("RunBenchmark")

package xyz.gmitch215.benchmarks.compiler

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.measurement.LIBRARY_DIRECTORY
import xyz.gmitch215.benchmarks.Language
import xyz.gmitch215.benchmarks.runCommand
import xyz.gmitch215.benchmarks.s
import java.io.File

suspend fun main(args: Array<String>): Unit = coroutineScope {
    logger.debug { "Arguments: ${args.joinToString { "'$it'" }}" }

    val config = File(args[0])
    LIBRARY_DIRECTORY = File(args[1])
    val language = args[2]
    val file = File(args[3])

    logger.debug { "Configuration File: ${config.absolutePath}" }

    if (!config.exists())
        error("Configuration file does not exist: ${config.absolutePath}")

    val languages = Yaml.default.decodeFromString<List<Language>>(config.readText())

    if (language.isNotBlank()) {
        logger.info { "Running with language '$language'" }
        val lang = languages.find { it.id == language } ?: error("Language not found: $language")

        if (file.isFile) {
            logger.info { "Running single file" }
            runFile(file, lang)
        } else {
            val files = file.walk().filter { it.isFile }.toList()
            logger.info { "Running ${files.size} files" }
            for (item in files)
                launch {
                    runFile(item, lang)
                }
        }

        return@coroutineScope
    }

    val benchmarks = file.walkTopDown().filter { it.isFile && "output" !in it.path && it.extension !in EXCLUDE_COMPILE }.toList()
    logger.info { "Running ${benchmarks.size} benchmarks" }
    for (benchmark in benchmarks)
        launch {
            val langs = languages.filter { it.fileName.substringAfter('.') == benchmark.extension }
            if (langs.isEmpty())
                error("Language not found: ${benchmark.extension}")

            for (lang in langs) {
                logger.info { "Compiling ${benchmark.absolutePath} under '${lang.id}'" }
                launch {
                    runFile(benchmark, lang)
                }
            }
        }
}

suspend fun runFile(file: File, lang: Language) {
    if (!file.exists())
        error("File does not exist: ${file.absolutePath}")

    var cmd = lang.absoluteRun
    logger.info { "Running ${file.absolutePath} under '${lang.id}'" }

    if (lang.file) {
        val folder = file.parentFile
        cmd = "${folder.absolutePath}${s}$cmd"
    }

    logger.debug { "Running Command '$cmd' for ${file.absolutePath}" }

    cmd.runCommand(file.parentFile, true)
}