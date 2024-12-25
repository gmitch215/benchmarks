import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlin.time.measureTime
import kotlinx.coroutines.runBlocking

fun main() {
    val client = HttpClient(CIO)

    val time = measureTime {
        runBlocking {
            try {
                client.get("http://httpbin.org/get")
            } catch (e: Exception) {}
        }
    }

    println(time.inWholeMilliseconds)
}