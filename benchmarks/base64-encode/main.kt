@file:OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)

import kotlin.time.measureTime
import kotlin.io.encoding.*

fun main() {
    val i1 = "Hello, World!"
    val i2 = "The quick brown fox jumps over the lazy dog."
    val i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-"

    val time = measureTime {
        Base64.Default.encode(i1.encodeToByteArray())
        Base64.Default.encode(i2.encodeToByteArray())
        Base64.Default.encode(i3.encodeToByteArray())
    }

    println(time.inWholeNanoseconds)
}