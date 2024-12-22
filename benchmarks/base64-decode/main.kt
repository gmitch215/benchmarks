@file:OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)

import kotlin.time.measureTime
import kotlin.io.encoding.*

fun main() {
    val i1 = "SGVsbG8sIFdvcmxkIQ=="
    val i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4="
    val i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ=="

    val time = measureTime {
        Base64.Default.decode(i1.encodeToByteArray())
        Base64.Default.decode(i2.encodeToByteArray())
        Base64.Default.decode(i3.encodeToByteArray())
    }

    println(time.inWholeNanoseconds)
}