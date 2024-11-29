import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        val url = URI.create("http://httpbin.org/get").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
    }

    println(time.inWholeMilliseconds)
}