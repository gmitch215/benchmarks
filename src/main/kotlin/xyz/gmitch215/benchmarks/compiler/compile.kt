@file:JvmName("CompileBenchmark")

package xyz.gmitch215.benchmarks.compiler

import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.EXCLUDE_FOLDERS
import xyz.gmitch215.benchmarks.measurement.LIBRARY_DIRECTORY
import xyz.gmitch215.benchmarks.Language
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
    "jar",
    "lockb",
    "lock",
    "obj",
    "log",
    "zip",
    "graalvm"
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

    val benchmarks = file.walkTopDown()
        .filter { it.isFile && it.extension !in EXCLUDE_COMPILE }
        .filter {
            for (folder in EXCLUDE_FOLDERS)
                if (it.path.contains(folder))
                    return@filter false

            return@filter true
        }
        .toList()
    logger.info { "Compiling ${benchmarks.size} benchmarks" }
    for (benchmark in benchmarks)
        launch {
            val langs = languages.filter { it.fileName.substringAfter('.') == benchmark.extension }
            if (langs.isEmpty())
                error("Language not found: '${benchmark.extension}' for ${benchmark.absolutePath}")

            for (lang in langs) {
                logger.info { "Compiling ${benchmark.absolutePath} under '${lang.id}'" }

                val dispatcher = Dispatchers.Default.limitedParallelism(lang.parallelLimit ?: 32, "Compile ${lang.id}")
                launch(dispatcher) {
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
            logger.warn { "Skipping compilation for ${file.absolutePath}: Already Compiled" }
            return
        }
    }

    val precompile = lang.absolutePrecompile
    if (precompile != null) {
        logger.info { "Precompiling ${file.absolutePath} under '${lang.id}'" }

        logger.debug { "Running Precompile Command '$precompile' for ${file.absolutePath}" }
        precompile.runCommand(file.parentFile, true)
        logger.info { "Precompilation complete for '${file.absolutePath}'" }
    }

    val cmd = lang.absoluteCompile
    if (cmd == null) {
        logger.info { "Skipping language '${lang.id}' as there is no compile command" }
        return
    }

    logger.info { "Compiling ${file.absolutePath} under '${lang.id}'" }

    logger.debug { "Running Compile Command '$cmd' for ${file.absolutePath}" }
    cmd.runCommand(file.parentFile, true)
    logger.info { "Compilation complete for '${file.absolutePath}'" }
}