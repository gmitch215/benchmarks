import kotlin.time.measureTime
import kotlinx.io.*
import kotlinx.io.files.*

fun main() {
    val path = Path("file.txt")

    val time = measureTime {
        SystemFileSystem.source(path).buffered().use {
            it.readString()
        }
    }

    println(time.inWholeNanoseconds)
}