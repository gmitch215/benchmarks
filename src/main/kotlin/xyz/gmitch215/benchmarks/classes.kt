package xyz.gmitch215.benchmarks

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import xyz.gmitch215.benchmarks.libraries.LibraryConfiguration
import xyz.gmitch215.benchmarks.measurement.LIBRARY_DIRECTORY
import java.io.File

@Serializable
data class BenchmarkConfiguration(
    val name: String,
    var id: String = name,
    val description: String,
    val measure: Measurement,
    val output: Measurement,
    val tags: List<String> = emptyList(),
    val disabled: List<String> = emptyList()
)

val windowsSuffixes = mapOf(
    "kotlin-jvm" to ".bat",
    "kotlin-native" to ".bat",
    "graalvm-precompile" to ".exe",
    "graalvm" to ".cmd"
)

@Serializable
data class Language(
    val language: String,
    val id: String,
    @SerialName("file-name")
    val fileName: String,
    val color: String,
    val file: Boolean = false,
    val location: String? = null,
    val run: String,
    @SerialName("append-exe")
    val appendExe: Boolean = false,
    val version: String,
    val precompile: String? = null,
    val compile: String? = null,
    @SerialName("compile-extra")
    val compileExtra: Map<String, String> = emptyMap(),
    val libraries: Map<String, String> = emptyMap(),
    val cleanup: List<String>? = null,
    @SerialName("parallel-limit")
    val parallelLimit: Int? = null
) {

    val absoluteVersion: String
        get() {
            var version0 = version
            if (location != null) {
                val home = System.getenv(location)
                if (home != null)
                    version0 = "${home}${s}bin${s}$version0"

                if (os == "windows") {
                    val executableSuffix = windowsSuffixes[id] ?: ".exe"
                    version0 = version0.replaceFirst(" ", "$executableSuffix ")
                }
            }

            return version0
        }

    fun librariesFlag(includeMain: Boolean = true): String? {
        if (libraries.isEmpty()) return null

        val flag = libraries["flag"] ?: error("No flag found for select libraries in '$id'")
        val repeat = libraries["repeat"]?.toBoolean() == true
        val escapePaths = libraries["escape-paths"]?.toBoolean() == true && os == "windows"
        val main = if (includeMain) libraries["main"] else null
        val separator = libraries["separator"] ?: if (os == "windows") ";" else ":"

        val subdir = libraries["${os}-${arch}"] ?: libraries[os] ?: libraries["default"] ?: id
        val dir = File(LIBRARY_DIRECTORY, subdir)

        if (!dir.exists())
            error("No library configuration found for ${os}-${arch} for '$id'")

        val files = mutableListOf<File>()

        if (libraries["local"]?.toBoolean() == true) {
            val config = Yaml.default.decodeFromString<LibraryConfiguration>(File(dir, "config.yml").readText())
            files.addAll(config.dependencies.flatMap { dep ->
                val namespace = dep.namespace

                if (dep.all) {
                    val files = File(dir, namespace).listFiles() ?: emptyArray()

                    files.filter { it.isFile }
                        .filter { it.extension == dep.fileType || dep.fileType == null }
                        .toList()
                } else
                    dep.paths.map {
                        val file = File(dir, "$namespace/$it")
                        if (!file.exists()) error("Library file does not exist: ${file.absolutePath}")

                        file
                    }
            })
        } else {
            val suffix = libraries["suffix"] ?: error("No file suffix found for select libraries in '$id'")
            files.addAll(dir.walkTopDown().filter { it.isFile && it.extension == suffix })
        }

        if (repeat) {
            val main0 = if (main != null) {
                if (escapePaths) "$flag\"$main\" " else "$flag$main "
            } else ""

            val path = files.joinToString(" $flag") {
                val p = if (escapePaths) "\"${it.absolutePath}\"" else it.absolutePath

                if (id == "rust") {
                    val name = it.nameWithoutExtension
                    val crate = name.substring(3).substringBefore('-')
                    return@joinToString "$crate=$p"
                }

                p
            }

            return "$main0$flag$path"
        } else {
            val files0 = files.joinToString(separator) { it.absolutePath }
            val prefix = if (main != null) "$main$separator" else ""
            val path = if (escapePaths) "\"$prefix$files0\"" else "$prefix$files0"

            return "$flag$path"
        }
    }

    val absolutePrecompile: String?
        get() {
            if (precompile == null) return null

            var precompile0 = precompile
            if (location != null) {
                val home = System.getenv(location)
                if (home != null)
                    precompile0 = "${home}${s}bin${s}$precompile0"

                if (os == "windows") {
                    val executableSuffix = windowsSuffixes["$id-precompile"] ?: windowsSuffixes[id] ?: ".exe"
                    precompile0 = precompile0.replaceFirst(" ", "$executableSuffix ")
                }
            }

            if (libraries.isNotEmpty()) {
                val includePrecompile = libraries["include-precompile"]?.toBoolean() == true
                if (includePrecompile) {
                    if (precompile0.contains(" ")) {
                        val (first, rest) = precompile0.split("\\s".toRegex(), limit = 2)
                        precompile0 = "$first ${librariesFlag()} $rest"
                    } else
                        precompile0 += " ${librariesFlag()}"
                }
            }

            return precompile0
        }

    val absoluteCompile: String?
        get() {
            if (compile == null) return null

            var compile0 = compile
            if (location != null) {
                val home = System.getenv(location)
                if (home != null)
                    compile0 = "${home}${s}bin${s}$compile0"

                if (os == "windows") {
                    val executableSuffix = windowsSuffixes[id] ?: ".exe"
                    compile0 = compile0.replaceFirst(" ", "$executableSuffix ")
                }
            }

            if (libraries.isNotEmpty()) {
                val flag = librariesFlag(false)
                if (flag != null && flag.isNotEmpty())
                    compile0 += " $flag"
            }

            if (compileExtra.isNotEmpty()) {
                val defaultExtra = compileExtra["default"]
                if (defaultExtra != null)
                    compile0 += " $defaultExtra"

                val archExtra = compileExtra["default-$arch"]
                if (archExtra != null)
                    compile0 += " $archExtra"

                val osExtra = compileExtra["$os-$arch"] ?: compileExtra[os]
                if (osExtra != null)
                    compile0 += " $osExtra"
            }

            return compile0
        }

    val absoluteRun: String
        get() {
            var run0 = run

            if (os == "windows" && appendExe)
                run0 += ".exe"

            if (id == "kotlin-jvm") {
                val home = System.getenv(location)
                if (home != null)
                    run0 = "${home}${s}bin${s}$run0"

                if (os == "windows")
                    run0 = run0.replaceFirst(" ", ".bat ")
            }

            if (libraries.isNotEmpty()) {
                val includeRun = libraries["include-run"]?.toBoolean() == true
                if (includeRun) {
                    if (run0.contains(" ")) {
                        val (first, rest) = run0.split("\\s".toRegex(), limit = 2)
                        run0 = "$first ${librariesFlag()} $rest"
                    } else
                        run0 += " ${librariesFlag()}"
                }
            }

            if (id == "kotlin-native")
                run0 += kotlinNativeSuffix

            if (';' in run0 && os != "windows")
                run0 = run0.replace(";", ":")

            return run0
        }

}