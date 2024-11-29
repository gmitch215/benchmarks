import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlin.time.measureTime

fun main() {
    val time = measureTime {
        val url = URI.create("http://httpbin.org/post").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        val data = "Hello, World!".toByteArray()
        connection.setRequestProperty("Content-Type", "text/plain")
        connection.setRequestProperty("Content-Length", data.size.toString())
        connection.outputStream.write(data)
    }

    println(time.inWholeMilliseconds)
}