plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
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
    implementation("com.charleskorn.kaml:kaml:0.62.2")
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    clean {
        delete("benchmarks/output")
    }

    // Benchmarking Tasks

    register("createBenchmarks", JavaExec::class) {
        mainClass.set("xyz.gmitch215.benchmarks.measurement.Benchmarker")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOf(
            file("benchmarks").absolutePath,
            project.findProperty("benchmarkFilter")?.toString()
        ).filterNotNull()
    }

    register("graphBenchmarks", JavaExec::class) {
        mustRunAfter("createBenchmarks")

        mainClass.set("xyz.gmitch215.benchmarks.site.Grapher")
        classpath = sourceSets["main"].runtimeClasspath
        args = listOf(
            file("benchmarks").absolutePath,
            project.findProperty("benchmarkFilter")?.toString()
        ).filterNotNull()
    }

    register("benchmark") {
        dependsOn(
            "createBenchmarks",
            "graphBenchmarks"
        )
    }
}