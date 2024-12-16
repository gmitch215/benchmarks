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
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.charleskorn.kaml:kaml:0.66.0")
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.1")

    runtimeOnly("ch.qos.logback:logback-classic:1.5.12")
    implementation("io.github.oshai:kotlin-logging:7.0.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val debug = project.findProperty("debug")?.toString()?.toBoolean() == true

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
        jvmArgs = listOf("-Xms6G", "-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("graphBenchmarks", JavaExec::class) {
        mustRunAfter("createBenchmarks")

        mainClass.set("xyz.gmitch215.benchmarks.site.Grapher")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            project.findProperty("benchmarkFilter")?.toString()
        )

        jvmArgs = listOf("-Xms6G", "-XX:+HeapDumpOnOutOfMemoryError")
    }

    register("benchmark") {
        dependsOn(
            "createBenchmarks",
            "graphBenchmarks"
        )
    }

    // Site Tasks

    register("copyResourcesToSite", Copy::class) {
        from("site")

        from("benchmarks/config.yml") {
            into("_data")
            rename { _ -> "langs.yml" }
        }

        filesMatching("benchmarks/*.yml") {
            exclude("config.yml")
            into("_data")
        }

        destinationDir = file("build/site")
    }

    register("createSiteData", JavaExec::class) {
        mustRunAfter("zipBenchmarks")

        mainClass.set("xyz.gmitch215.benchmarks.site.DataCreator")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("benchmarks").absolutePath,
            file("build/benchmarks").absolutePath,
            file("build/site/_data").absolutePath
        )
    }

    register("moveGraphs", Copy::class) {
        mustRunAfter("graphBenchmarks", "copyResourcesToSite", "createSiteData")

        from("build/site/_data/results/windows/graphs/") {
            into("assets/graphs/windows/")
        }

        from("build/site/_data/results/macos/graphs/") {
            into("assets/graphs/macos/")
        }

        from("build/site/_data/results/linux/graphs/") {
            into("assets/graphs/linux/")
        }

        doLast {
            delete("build/site/_data/results/windows/graphs/")
            delete("build/site/_data/results/macos/graphs/")
            delete("build/site/_data/results/linux/graphs/")
        }

        destinationDir = file("build/site")
    }

    register("generatePages", JavaExec::class) {
        dependsOn("moveGraphs")
        mustRunAfter("copyResourcesToSite", "createSiteData")

        mainClass.set("xyz.gmitch215.benchmarks.site.PagesCreator")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOfNotNull(
            file("build/site/_data").absolutePath,
            file("build/site").absolutePath
        )
    }

    register("site") {
        mustRunAfter("zipBenchmarks")

        dependsOn(
            "copyResourcesToSite",
            "generatePages",
            "createSiteData"
        )
    }

    // Site Preview Tasks

    register("zipBenchmarks", Zip::class) {
        mustRunAfter("createBenchmarks", "graphBenchmarks")

        from("benchmarks/output")

        archiveFileName.set("benchmarks-${DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()}.zip")
        destinationDirectory.set(file("build/benchmarks"))
    }

    register("preview") {
        dependsOn("zipBenchmarks", "site")
    }

}