import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "xyz.gmitch215"
version = "1.0.0"
description = "Various benchmarks on programming languages"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("com.charleskorn.kaml:kaml:0.67.0")
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.1")
    implementation("com.github.oshi:oshi-core:6.6.6")

    runtimeOnly("ch.qos.logback:logback-classic:1.5.16")
    implementation("io.github.oshai:kotlin-logging:7.0.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val debug = project.findProperty("debug")?.toString()?.toBoolean() == true
val os = when (val name = DefaultNativePlatform.getCurrentOperatingSystem().name.lowercase()) {
    "osx", "mac os x", "darwin" -> "mac"
    else -> name.substringBefore(' ').trim()
}

tasks {
    clean {
        delete("bin")
        delete("benchmarks/output")
        delete("logs")

        delete(
            fileTree("benchmarks") {
                include("**/*.exe")
                include("**/*.kexe")
                include("**/*.o")
                include("**/*.class")
                include("**/*.jar")
                include("**/*.pdb")
                include("main.kt.kexe.*/**")
            }
        )

        delete(
            fileTree("lib") {
                include("**/*.dll")
                include("**/*.dylib")
                include("**/*.so")
                include("**/*.jar")
                include("**/*.klib")
                include("**/*.pdb")
                include("**/*.exe")
                include("**/*.rlib")
                include("**/*.rmeta")
                include("**/*.d")
            }
        )
    }

    processResources {
        filesMatching("simplelogger.properties") {
            expand(mapOf("logLevel" to if (debug) "debug" else "info"))
        }
    }

    register("validate", JavaExec::class) {
        mainClass.set("xyz.gmitch215.benchmarks.measurement.Validator")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath
        )
        jvmArgs = listOf("-XX:+HeapDumpOnOutOfMemoryError")

        val output = File(layout.buildDirectory.asFile.get(), ".validated")

        outputs.upToDateWhen { output.exists() }
        outputs.file(output)

        doLast { output.writeText("true") }
    }

    register("downloadLibraries", JavaExec::class) {
        mainClass.set("xyz.gmitch215.benchmarks.libraries.Downloader")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("lib").absolutePath
        )
        jvmArgs = listOf("-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("compileBenchmark", JavaExec::class) {
        dependsOn("downloadLibraries")

        val language = project.findProperty("language")?.toString() ?: ""
        val file = project.findProperty("file")?.toString()

        mainClass.set("xyz.gmitch215.benchmarks.compiler.CompileBenchmark")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks/config.yml").absolutePath,
            file("lib").absolutePath,
            language,
            file("benchmarks${if (file != null) "/$file" else ""}").absolutePath
        )
        jvmArgs = listOf("-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("runBenchmark", JavaExec::class) {
        dependsOn("downloadLibraries")
        mustRunAfter("compileBenchmark")

        val language = project.findProperty("language")?.toString() ?: ""
        val file = project.findProperty("file")?.toString()

        mainClass.set("xyz.gmitch215.benchmarks.compiler.RunBenchmark")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks/config.yml").absolutePath,
            file("lib").absolutePath,
            language,
            file("benchmarks${if (file != null) "/$file" else ""}").absolutePath
        )
        jvmArgs = listOf("-XX:+HeapDumpOnOutOfMemoryError")
    }

    // Benchmarking Tasks

    register("createBenchmarks", JavaExec::class) {
        dependsOn("validate", "compileBenchmark", "downloadLibraries")

        mainClass.set("xyz.gmitch215.benchmarks.measurement.Benchmarker")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            file("lib").absolutePath,
            project.findProperty("benchmarkFilter")?.toString()
        )
        jvmArgs = listOf("-Xms512M", "-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("graphBenchmarks", JavaExec::class) {
        mustRunAfter("createBenchmarks")

        mainClass.set("xyz.gmitch215.benchmarks.site.Grapher")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            project.findProperty("benchmarkFilter")?.toString()
        )

        jvmArgs = listOf("-Xms512M", "-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("benchmark") {
        dependsOn(
            "validate",
            "createBenchmarks",
            "graphBenchmarks"
        )
    }

    // Site Tasks

    register("createSite", JavaExec::class) {
        mustRunAfter("copyPreview")

        mainClass.set("xyz.gmitch215.benchmarks.site.SiteCreator")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            file("build/site").absolutePath
        )
    }

    register("moveGraphs") {
        mustRunAfter("createSite", "copyPreview")

        doFirst {
            copy {
                into("build/site")

                from("build/site/_data/results/windows/graphs/") {
                    into("assets/graphs/windows/")
                }

                from("build/site/_data/results/mac/graphs/") {
                    into("assets/graphs/mac/")
                }

                from("build/site/_data/results/linux/graphs/") {
                    into("assets/graphs/linux/")
                }
            }
        }

        doLast {
            delete("build/site/_data/results/windows/graphs/")
            delete("build/site/_data/results/mac/graphs/")
            delete("build/site/_data/results/linux/graphs/")
        }
    }

    register("copyResourcesToSite") {
        mustRunAfter("moveGraphs", "createSite", "copyPreview")

        doFirst {
            copy {
                into("build/site")

                from("site")

                from("benchmarks/config.yml") {
                    into("_data")
                    rename { _ -> "langs.yml" }
                }

                filesMatching("benchmarks/*.yml") {
                    exclude("config.yml")
                    into("_data")
                }
            }
        }
    }

    register("site") {
        dependsOn(
            "createSite",
            "moveGraphs",
            "copyResourcesToSite"
        )
    }

    // Site Preview Tasks

    register("copyPreview", Copy::class) {
        mustRunAfter("createBenchmarks", "graphBenchmarks", "clean")
        from("benchmarks/output")

        destinationDir = file("build/site/_data/results/$os")
    }

    register("serve", Exec::class) {
        mustRunAfter("site")

        workingDir = file("build/site")

        val suffix = if (os == "windows") ".bat" else ""
        commandLine("bundle${suffix}", "exec", "jekyll", "serve", "--livereload")
    }

    register("preview") {
        dependsOn("site", "copyPreview", "serve")
    }

}
