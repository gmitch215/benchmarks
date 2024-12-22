@file:JvmName("Validator")

package xyz.gmitch215.benchmarks.measurement

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.runCommand
import java.io.File
import java.nio.file.Files

suspend fun main(args: Array<String>) = withContext(Dispatchers.IO) {
    logger.info { "Starting Validator" }
    logger.debug { "Arguments: ${args.joinToString()}" }

    if (args.size != 1)
        error("Expected 1 argument, got ${args.size}")

    val config = File("${args[0]}/config.yml")
    logger.debug { "Config File: ${config.absolutePath}" }

    if (!config.exists())
        error("Config file does not exist: ${config.absolutePath}")

    val languages = Yaml.default.decodeFromString<List<Language>>(config.readText())
    val temp = Files.createTempDirectory(null).toFile()
    coroutineScope {
        for (lang in languages)
            launch {
                val version = lang.absoluteVersion

                logger.info { "Validating ${lang.id}..." }
                logger.debug { "Running Version Command for '${lang.id}': $version" }

                try {
                    val result = version.runCommand(temp)?.trim() ?: error("Failed to run version command: $version")
                    logger.debug { "Version Command Output: $result" }
                } catch (e: Exception) {
                    throw IllegalStateException("Missing language: ${lang.language} (${lang.id})", e)
                }

                logger.info { "Successfully validated ${lang.id}" }
            }
    }
    temp.delete()
    logger.info { "Successfully validated all languages" }
}