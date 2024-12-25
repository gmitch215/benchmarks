@file:JvmName("CompileBenchmark")

package xyz.gmitch215.benchmarks.compiler

import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.measurement.LIBRARY_DIRECTORY
import xyz.gmitch215.benchmarks.measurement.Language
import xyz.gmitch215.benchmarks.runCommand
import java.io.File

val EXCLUDE_COMPILE = listOf(
    "yml",
    "bat",
    "sh",
    "md",
    "txt",
    "o",
    "exe",
    "kexe",
    "pdb",
    "class",
    "json",
    "jar"
)

val logger = KotlinLogging.logger("BenchmarkCompiler")

suspend fun main(args: Array<String>): Unit = coroutineScope {
    logger.debug { "Arguments: ${args.joinToString()}" }

    val config = File(args[0])
    LIBRARY_DIRECTORY = File(args[1])
    val language = args[2]
    val file = File(args[3])

    logger.debug { "Configuration File: ${config.absolutePath}" }

    if (!config.exists())
        error("Configuration file does not exist: ${config.absolutePath}")

    val languages = Yaml.default.decodeFromString<List<Language>>(config.readText())

    if (language.isNotBlank()) {
        logger.info { "Compiling with language '$language'" }
        val lang = languages.find { it.id == language } ?: error("Language not found: $language")

        if (file.isFile) {
            logger.info { "Compiling single file" }
            compileFile(file, lang)
        } else {
            val files = file.walk().filter { it.isFile }.toList()
            logger.info { "Compiling ${files.size} files" }
            for (item in files)
                launch {
                    compileFile(item, lang)
                }
        }

        return@coroutineScope
    }

    val benchmarks = file.walkTopDown().filter { it.isFile && "output" !in it.path && it.extension !in EXCLUDE_COMPILE }.toList()
    logger.info { "Compiling ${benchmarks.size} benchmarks" }
    for (benchmark in benchmarks)
        launch {
            val langs = languages.filter { it.fileName.substringAfter('.') == benchmark.extension }
            if (langs.isEmpty())
                error("Language not found: ${benchmark.extension}")

            for (lang in langs) {
                logger.info { "Compiling ${benchmark.absolutePath} under '${lang.id}'" }
                launch {
                    compileFile(benchmark, lang)
                }
            }
        }
}

suspend fun compileFile(file: File, lang: Language) {
    if (!file.exists())
        error("File does not exist: ${file.absolutePath}")

    if (lang.cleanup != null) {
        val outputs = lang.cleanup.map { File(file.parentFile, it) }
        logger.debug { "Checking for compiled files: ${outputs.joinToString()}" }
        if (outputs.any { it.exists() }) {
            logger.info { "Skipping compilation of '${lang.id}' for ${file.absolutePath}: Already Compiled" }
            return
        }
    }

    var cmd = lang.absoluteCompile
    if (cmd == null) {
        logger.info { "Skipping language '${lang.id}' as there is no compile command" }
        return
    }

    logger.info { "Compiling ${file.absolutePath} under '${lang.id}'" }

    logger.debug { "Running Compile Command '$cmd' for ${file.absolutePath}" }
    cmd.runCommand(file.parentFile, true)
    logger.info { "Compilation complete for '${file.absolutePath}'" }
}