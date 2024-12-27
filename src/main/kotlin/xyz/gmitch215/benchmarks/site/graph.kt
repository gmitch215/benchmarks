@file:JvmName("Grapher")

package xyz.gmitch215.benchmarks.site

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.groupBy
import org.jetbrains.kotlinx.kandy.dsl.categorical
import org.jetbrains.kotlinx.kandy.dsl.internal.LayerCreatorScope
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.toHTML
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layers.builders.BarsBuilder
import org.jetbrains.kotlinx.kandy.letsplot.layers.line
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import org.jetbrains.kotlinx.kandy.letsplot.layers.text
import org.jetbrains.kotlinx.kandy.letsplot.settings.Symbol
import org.jetbrains.kotlinx.kandy.letsplot.style.Theme
import org.jetbrains.kotlinx.kandy.letsplot.x
import org.jetbrains.kotlinx.kandy.letsplot.y
import org.jetbrains.kotlinx.kandy.util.color.Color
import xyz.gmitch215.benchmarks.BenchmarkResult
import xyz.gmitch215.benchmarks.logger
import xyz.gmitch215.benchmarks.measurement.Language
import xyz.gmitch215.benchmarks.measurement.RUN_COUNT
import xyz.gmitch215.benchmarks.measurement.json
import xyz.gmitch215.benchmarks.repeat
import xyz.gmitch215.benchmarks.times
import java.io.File
import kotlin.collections.first

val PRIMARY_GRAPH_SIZE = 1200 to 600
val SECONDARY_GRAPH_SIZE = 1000 to 600
val STYLE = """
    |<style>
    |html, body, div {
    |    margin: 0;
    |    padding: 0;
    |    width: 100%;
    |    height: 100%;
    |}
    |</style>
""".trimMargin()

const val BAR_LABEL_OFFSET = 35
const val BAR_LABEL_SIZE = 6.5

suspend fun main(args: Array<String>): Unit = coroutineScope {
    val rootDir = File(args[0])
    val outputDir = File(rootDir, "output")

    val filter = args.getOrNull(1)?.let { Regex(it) }
    if (filter != null)
        logger.info { "Filtering graphs with '$filter'" }

    if (!outputDir.exists()) {
        logger.debug { "Output directory '${outputDir.absolutePath}' does not exist" }
        error("Output Directory does not exist")
    }

    val configFile = File(rootDir, "config.yml").readText(Charsets.UTF_8)
    logger.debug { "Configuration File: \n$configFile\n" }

    val config = Yaml.default.decodeFromString<List<Language>>(configFile)

    val graphsFolder = File(outputDir, "graphs")
    graphsFolder.mkdirs()

    val folders = rootDir.listFiles { f -> f?.let { it.isDirectory && it.name != "output" } == true } ?: emptyArray()

    val benchmarkLocations = mutableMapOf<String, List<Pair<String, String>>>()
    for (run in config) {
        val id = run.id
        for (folder in folders) {
            val name = folder.name
            if (filter != null && !filter.matches(name))
                continue

            val location = File(outputDir, "${id}/$name.json")
            if (!location.exists()) {
                logger.debug { "Skipping $id for $name, no data found or disabled" }
                continue
            }

            if (benchmarkLocations.contains(name)) {
                benchmarkLocations[name] = (benchmarkLocations[name] ?: emptyList()) + Pair(run.language, location.absolutePath)
                logger.debug { "Adding $id for $name at ${location.absolutePath}" }
            } else {
                benchmarkLocations[name] = listOf(Pair(run.language, location.absolutePath))
            }
        }
    }

    // Create Benchmark Graphs
    for ((name, benchmarks) in benchmarkLocations)
        launch {
            createBenchmarkGraphs(benchmarks, File(graphsFolder, name))
        }

    // Create Versus Graphs
    for ((name, benchmarks) in benchmarkLocations)
        launch {
            createVersusGraphs(benchmarks, config, File(graphsFolder, name))
        }

    // Create Rank Graph
    launch {
        val file = File(outputDir, "rankings.json")
        if (!file.exists())
            error("Rankings data file does not exist: ${file.absolutePath}")

        val rankings = file.readText(Charsets.UTF_8)
        createRanksGraph(json.decodeFromString(rankings), graphsFolder)
    }
}

suspend fun createBenchmarkGraphs(benchmarks: List<Pair<String, String>>, out: File) = withContext(Dispatchers.IO) {
    val data0 = mutableListOf<Pair<String, BenchmarkResult>>()
    val mutex = Mutex()

    launch {
        for ((language, location) in benchmarks) {
            launch {
                val file = File(location)
                if (!file.exists())
                    error("Benchmark Data does not exist: ${file.absolutePath}")

                val content = file.readText(Charsets.UTF_8)

                mutex.withLock {
                    data0.add(Pair(language, json.decodeFromString(content)))
                }
            }
        }
    }.join()

    val data = data0.sortedBy { it.first }
    out.mkdirs()

    val languageColors = data.map {
        val hex = Integer.parseInt(it.second.languageColor, 16)
        val color = Color.rgb(hex shr 16 and 0xFF, hex shr 8 and 0xFF, hex and 0xFF)
        return@map Pair(it.first, color)
    }

    val example = data.first().second

    // Create All-Time Graph
    launch {
        val runs = (List(RUN_COUNT) { it + 1 }) * data.size
        val labels = data.map { it.first }.repeat(RUN_COUNT)

        val values = data.flatMap { pair ->
            val result = pair.second
            val results = result.results.map { (it * result.measure.multiplier) / result.output.multiplier }.toMutableList()
            if (results.size != RUN_COUNT) {
                // Data loss, add average value
                val avg = results.average()
                results.addAll(List(RUN_COUNT - results.size) { avg })
            }

            return@flatMap results
        }

        val allTime = dataFrameOf(
            "runs" to runs,
            "language" to labels,
            "time" to values
        ).groupBy("language").plot {
            layout {
                title = example.name
                caption = "All-time Graph over $RUN_COUNT runs in ${example.output.name.lowercase()}"
                theme = Theme.HIGH_CONTRAST_DARK
                size = PRIMARY_GRAPH_SIZE
            }

            x(runs) {
                axis.name = "Run Index"
                axis.breaksLabeled(runs, runs.map { "#${it}" })
            }

            y("time") {
                axis.name = "Time (${example.output.unit})"
            }

            line {
                color(labels) {
                    legend.name = "Language"
                    scale = categorical(languageColors.map { it.second }, languageColors.map { it.first })
                }

                width = 2.0
            }

            points {
                size = 4.0

                color(labels) {
                    symbol = Symbol.CIRCLE
                    scale = categorical(languageColors.map { it.second }, languageColors.map { it.first })
                }
            }
        }

        val allTimeF = File(out, "all-time.html")

        logger.info { "Writing graph to ${allTimeF.absolutePath}" }
        allTimeF.writeText("${allTime.toHTML(false)}$STYLE")
    }

    val labels = data.map { it.first }

    fun BarsBuilder.settings() {
        fillColor(labels) {
            legend.name = "Language"
            scale = categorical(languageColors.map { it.second }, languageColors.map { it.first })
        }

        borderLine.apply {
            color = Color.WHITE
            width = 0.75
        }
    }

    fun LayerCreatorScope.labels(labels: List<String>, values: List<Double>) {
        text {
            x(labels)

            val yOffset = values.max() / BAR_LABEL_OFFSET
            y(values.map { it + yOffset })

            label(values.map { example.output.formatOutput(it, 2, true) })
            font.size = BAR_LABEL_SIZE
        }
    }

    // Create Average Graph
    launch {
        val values = data.map { it.second.avgDouble }

        val average = dataFrameOf(
            "language" to labels,
            "time" to values
        ).plot {
            bars {
                layout {
                    title = "${example.name} - Averages"
                    caption = "Average Time over $RUN_COUNT runs in ${example.output.name.lowercase()}"
                    theme = Theme.HIGH_CONTRAST_DARK
                    size = SECONDARY_GRAPH_SIZE
                }

                x("language") {
                    axis.name = "Language"
                }

                y("time") {
                    axis.name = "Average Time (${example.output.unit})"
                }

                settings()
            }

            labels(labels, values)
        }

        val averageF = File(out, "average.html")

        logger.info { "Writing graph to ${averageF.absolutePath}" }
        averageF.writeText("${average.toHTML(false)}$STYLE")
    }

    // Create Median Graph
    launch {
        val values = data.map { (it.second.results.sorted().let { results ->
            val size = results.size
            if (size % 2 == 0) {
                (results[size / 2] + results[size / 2 - 1]) / 2
            } else {
                results[size / 2]
            }
        } * it.second.measure.multiplier) / it.second.output.multiplier }

        val median = dataFrameOf(
            "language" to labels,
            "time" to values
        ).plot {
            bars {
                layout {
                    title = "${example.name} - Median"
                    caption = "Median Time over $RUN_COUNT runs in ${example.output.name.lowercase()}"
                    theme = Theme.HIGH_CONTRAST_DARK
                    size = SECONDARY_GRAPH_SIZE
                }

                x("language") {
                    axis.name = "Language"
                }

                y("time") {
                    axis.name = "Median Time (${example.output.unit})"
                }

                settings()
            }

            labels(labels, values)
        }

        val medianF = File(out, "median.html")

        logger.info { "Writing graph to ${medianF.absolutePath}" }
        medianF.writeText("${median.toHTML(false)}$STYLE")
    }

    // Create Low Graph
    launch {
        val labels = data.map { it.first }
        val values = data.map { it.second.lowDouble }

        val low = dataFrameOf(
            "language" to labels,
            "time" to values
        ).plot {
            bars {
                layout {
                    title = "${example.name} - Best Case"
                    caption = "Lowest Time from $RUN_COUNT runs in ${example.output.name.lowercase()}"
                    theme = Theme.HIGH_CONTRAST_DARK
                    size = SECONDARY_GRAPH_SIZE
                }

                x("language") {
                    axis.name = "Language"
                    axis.breaks()
                }

                y("time") {
                    axis.name = "Low Time (${example.output.unit})"
                }

                settings()
            }

            labels(labels, values)
        }

        val lowF = File(out, "low.html")

        logger.info { "Writing graph to ${lowF.absolutePath}" }
        lowF.writeText("${low.toHTML(false)}$STYLE")
    }

    // Create High Graph
    launch {
        val labels = data.map { it.first }
        val values = data.map { it.second.highDouble }

        val high = dataFrameOf(
            "language" to labels,
            "time" to values
        ).plot {
            bars {
                layout {
                    title = "${example.name} - Worst Case"
                    caption = "Highest Time from $RUN_COUNT runs in ${example.output.name.lowercase()}"
                    theme = Theme.HIGH_CONTRAST_DARK
                    size = SECONDARY_GRAPH_SIZE
                }

                x("language") {
                    axis.name = "Language"
                    axis.breaks()
                }

                y("time") {
                    axis.name = "High Time (${example.output.unit})"
                }

                settings()
            }

            labels(labels, values)
        }

        val highF = File(out, "high.html")

        logger.info { "Writing graph to ${highF.absolutePath}" }
        highF.writeText("${high.toHTML(false)}$STYLE")
    }
}

suspend fun createVersusGraphs(benchmarks: List<Pair<String, String>>, runs: List<Language>, out: File) = withContext(Dispatchers.IO) {
    val pairs = mutableListOf<Pair<Language, Language>>()
    for (i in 0 until runs.size) {
        for (j in i + 1 until runs.size) {
            pairs.add(runs[i] to runs[j])
        }
    }

    val data = mutableMapOf<String, BenchmarkResult>()
    val mutex = Mutex()

    launch {
        for ((language, location) in benchmarks) {
            launch {
                val file = File(location)
                if (!file.exists())
                    error("Benchmark Data does not exist: ${file.absolutePath}")

                val content = file.readText(Charsets.UTF_8)

                mutex.withLock {
                    data[language] = json.decodeFromString(content)
                }
            }
        }
    }.join()

    out.mkdirs()

    val languageColors = data.map {
        val hex = Integer.parseInt(it.value.languageColor, 16)
        val color = Color.rgb(hex shr 16 and 0xFF, hex shr 8 and 0xFF, hex and 0xFF)
        return@map Pair(it.key, color)
    }

    val example = data.values.first()

    launch {
        for (match in pairs)
            launch {
                val l1: Language; val l2: Language
                if (match.first.id < match.second.id) {
                    l1 = match.first
                    l2 = match.second
                } else {
                    l1 = match.second
                    l2 = match.first
                }

                val values = data.filterValues { it.languageId == l1.id || it.languageId == l2.id }
                    .toList()
                    .sortedBy { it.first }
                    .flatMap { pair ->
                        val result = pair.second
                        val results = result.results.map { (it * result.measure.multiplier) / result.output.multiplier }.toMutableList()
                        if (results.size != RUN_COUNT) {
                            // Data loss, add average value
                            val avg = results.average()
                            results.addAll(List(RUN_COUNT - results.size) { avg })
                        }

                        return@flatMap results
                    }

                // Language was skipped
                if (values.size != RUN_COUNT * 2) return@launch

                val colors = listOf(l1, l2).map { languageColors.first { (lang, _) -> lang == it.language } }
                val runs = (List(RUN_COUNT) { it + 1 }) * 2
                val labels = listOf(l1.language, l2.language).repeat(RUN_COUNT)

                val versus = dataFrameOf(
                    "runs" to runs,
                    "language" to labels,
                    "time" to values
                ).groupBy("language").plot {
                    layout {
                        title = "${l1.language} vs ${l2.language} - ${example.name}"
                        caption = "${l1.language} and ${l2.language}, against $RUN_COUNT runs in ${example.output.name.lowercase()}"
                        theme = Theme.HIGH_CONTRAST_DARK
                        size = PRIMARY_GRAPH_SIZE
                    }

                    x(runs) {
                        axis.name = "Run Index"
                        axis.breaksLabeled(runs, runs.map { "#${it}" })
                    }

                    y("time") {
                        axis.name = "Time (${example.output.unit})"
                    }

                    line {
                        color(labels) {
                            legend.name = "Match"
                            scale = categorical(colors.map { it.second }, colors.map { it.first })
                        }

                        width = 2.0
                    }

                    points {
                        size = 6.0

                        color(labels) {
                            symbol = Symbol.CIRCLE
                            scale = categorical(colors.map { it.second }, colors.map { it.first })
                        }
                    }
                }

                val file = File(out, "${l1.id}-vs-${l2.id}.html")

                logger.info { "Writing versus graph [${l1.language}, ${l2.language}] to ${file.absolutePath}" }
                file.writeText("${versus.toHTML(false)}$STYLE")
            }
    }.join()

    logger.info { "Finished writing versus graphs in ${out.absolutePath}" }
}

suspend fun createRanksGraph(rankings: JsonObject, out: File) = withContext(Dispatchers.IO) {
    val ranks = mutableMapOf<String, Int>()

    for ((name, run) in rankings) {
        if (name == "overall") continue
        val scores = run.jsonObject["verbose"]!!.jsonObject.mapValues { it.value.jsonObject["rank"]!!.jsonPrimitive.int }
        for ((language, score) in scores) {
            ranks[language] = ranks.getOrDefault(language, 0) + score
        }
    }

    val sorted = ranks.toList().sortedBy { it.second }
    val labels = sorted.map { it.first }
    val values = sorted.map { it.second }

    val rankLabels = labels.mapIndexed { i, _ -> "#${i + 1}" }

    val rank = dataFrameOf(
        "language" to labels,
        "score" to values
    ).plot {
        bars {
            layout {
                title = "Overall Rankings"
                caption = "Score is calculated from their rank against other languages added together (lower is better)"
                theme = Theme.HIGH_CONTRAST_DARK
                size = PRIMARY_GRAPH_SIZE
            }

            x("language") {
                axis.name = "Language"
            }

            y("score") {
                axis.name = "Score"
            }

            fillColor(labels) {
                legend.name = "Language"
                scale = categorical(rankLabels.map {
                    when (it) {
                        "#1" -> Color.hex("#ffd700")
                        "#2" -> Color.hex("#c0c0c0")
                        "#3" -> Color.hex("#cd7f32")
                        else -> Color.BLUE
                    }
                }, labels)
            }

            borderLine.apply {
                color = Color.WHITE
                width = 0.75
            }

            text {
                x(labels)

                val yOffset = values.max() / BAR_LABEL_OFFSET
                y(values.map { it + yOffset })

                label(rankLabels.mapIndexed { i, l -> "$l - ${values[i]}" })
                font.size = 6.5
            }
        }
    }

    val rankF = File(out, "rank.html")

    logger.info { "Writing graph to ${rankF.absolutePath}" }
    rankF.writeText("${rank.toHTML(false)}$STYLE")
}