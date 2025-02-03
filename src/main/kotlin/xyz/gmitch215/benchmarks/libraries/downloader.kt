@file:JvmName("Downloader")

package xyz.gmitch215.benchmarks.libraries

import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.runCommand
import java.io.File
import java.net.URI

val logger = KotlinLogging.logger("LibraryDownloader")

suspend fun main(args: Array<String>): Unit = coroutineScope {
    logger.info { "Downloading libraries" }
    logger.debug { "Arguments: ${args.joinToString()}" }

    val lib = File(args[0])

    logger.debug { "Library configuration directory: ${lib.absolutePath}" }

    if (!lib.exists())
        error("Library configuration directory does not exist")

    val configs = lib.walkTopDown().filter { it.name == "config.yml" }

    for (config in configs)
        launch(Dispatchers.IO) {
            logger.info { "Processing library configuration: ${config.absolutePath}" }

            val configuration = Yaml.default.decodeFromString<LibraryConfiguration>(config.readText())

            val setup = configuration.setup
            if (setup != null) {
                logger.info { "Running library setup for ${config.absolutePath}" }
                setup.runCommand(config.parentFile, true)
                logger.info { "Library setup complete for ${config.absolutePath}" }
            }

            if (configuration.type == "local") {
                logger.info { "Skipping library download, using local files: ${config.absolutePath}" }
                return@launch
            }

            val repository = configuration.repository ?: error("Repository not specified for non-local library: ${config.absolutePath}")

            coroutineScope {
                for ((name, path) in configuration.paths) {
                    launch {
                        val file = File(config.parentFile, name)
                        if (file.exists()) {
                            logger.debug { "Library already downloaded: ${file.absolutePath}" }
                            return@launch
                        }

                        val url = URI.create("$repository/$path").toURL()
                        logger.info { "Downloading library: $url" }

                        val bytes = url.openStream().use { it.readBytes() }
                        file.writeBytes(bytes)

                        logger.info { "Library downloaded: ${file.absolutePath}" }
                    }
                }
            }

            logger.info { "Library configuration processed: ${config.absolutePath}" }
        }
}

@Serializable
data class LibraryConfiguration(
    val type: String,
    val setup: String? = null,
    val repository: String? = null,
    val dependencies: List<DependencyConfiguration>
) {

    val paths: Map<String, String>
        get() {
            val map = mutableMapOf<String, String>()

            for (dependency in dependencies) {
                val namespace = dependency.namespace
                val info = if (dependency.version != null) "-${dependency.version}" else ""

                for (path in dependency.paths) {
                    val path0 = path.substringBefore(';')
                    val suffix = if (';' in path) "-${path.substringAfter(';')}" else ""

                    val name = "$path0$info$suffix.$type"

                    val (path1, version) =
                        if (dependency.version != null)
                            Pair(path0, dependency.version)
                        else
                            Pair(path0.substringBeforeLast('-'), path0.substringAfterLast('-'))

                    val realPath = "$namespace/$path1/$version/$name"

                    map[name] = realPath
                }
            }

            return map
        }

}

@Serializable
data class DependencyConfiguration(
    val namespace: String,
    val version: String? = null,
    val all: Boolean = false,
    @SerialName("file-type")
    val fileType: String? = null,
    val paths: List<String> = emptyList()
)