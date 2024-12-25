@file:JvmName("CompileBenchmark")

package xyz.gmitch215.benchmarks

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.measurement.LIBRARY_DIRECTORY
import xyz.gmitch215.benchmarks.measurement.Language
import java.io.File

suspend fun main(args: Array<String>): Unit = coroutineScope {
    logger.debug { "Arguments: ${args.joinToString()}" }

    val config = File(args[0])
    LIBRARY_DIRECTORY = File(args[1])
    val language = args[2]
    val file = File(args[3])

    logger.debug { "Configuration file: ${config.absolutePath}" }
    logger.info { "Compiling ${file.absolutePath} under '$language'" }

    if (!config.exists())
        error("Configuration file does not exist: ${config.absolutePath}")

    val languages = Yaml.default.decodeFromString<List<Language>>(config.readText())
    val lang = languages.find { it.id == language } ?: error("Language ID not found: $language")

    if (!file.exists())
        error("File does not exist: ${file.absolutePath}")

    var cmd = lang.absoluteCompile
    if (cmd == null)
        error("No compile command found for language '${lang.language}'")

    if (language == "kotlin-native")
        cmd += " -verbose"

    logger.debug { "Running Compile Command '$cmd'" }
    cmd.runCommand(file.parentFile, true)
    logger.info { "Compilation complete" }
}