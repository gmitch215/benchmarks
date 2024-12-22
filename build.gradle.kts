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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.charleskorn.kaml:kaml:0.66.0")
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.1")
    implementation("com.github.oshi:oshi-core:6.6.5")

    runtimeOnly("ch.qos.logback:logback-classic:1.5.14")
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
    }

    processResources {
        filesMatching("simplelogger.properties") {
            expand(mapOf("logLevel" to if (debug) "debug" else "info"))
        }
    }

    // Benchmarking Tasks

    register("createBenchmarks", JavaExec::class) {
        mainClass.set("xyz.gmitch215.benchmarks.measurement.Benchmarker")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
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
            "createBenchmarks",
            "graphBenchmarks"
        )
    }

    // Site Tasks

    register("createSite", JavaExec::class) {
        mustRunAfter("preview")

        mainClass.set("xyz.gmitch215.benchmarks.site.SiteCreator")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            file("build/site").absolutePath
        )
    }

    register("moveGraphs") {
        mustRunAfter("createSite", "preview")

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
        mustRunAfter("moveGraphs", "createSite", "preview")

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

    register("preview", Copy::class) {
        from("benchmarks/output")

        destinationDir = file("build/site/_data/results/$os")
    }

}
