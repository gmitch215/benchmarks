@file:OptIn(
    dev.whyoleg.cryptography.DelicateCryptographyApi::class,
    dev.whyoleg.cryptography.CryptographyProviderApi::class,
    kotlin.io.encoding.ExperimentalEncodingApi::class
)

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.MD5
import kotlin.time.measureTime
import kotlinx.coroutines.runBlocking

val phrases = listOf(
    "Seize the day",
    "Time flies quickly",
    "Reach for the stars",
    "Love conquers all",
    "Break the ice",
    "A blessing in disguise",
    "Actions speak louder than words",
    "Every cloud has a silver lining",
    "Burn the midnight oil",
    "Jack of all trades",
    "Piece of cake",
    "Bite the bullet",
    "Crystal clear",
    "Better late than never",
    "The early bird catches the worm",
    "Cat got your tongue",
    "A penny for your thoughts",
    "Laughter is the best medicine",
    "All ears",
    "Barking up the wrong tree",
    "Beauty is in the eye of the beholder",
    "The calm before the storm",
    "Let the cat out",
    "On thin ice",
    "Cut to the chase"
)

fun main() {
    val hasher = CryptographyProvider.Default
        .get(MD5)
        .hasher()

    val time = measureTime {
        runBlocking {
            for (phrase in phrases)
                hasher.hash(phrase.encodeToByteArray())
        }
    }

    println(time.inWholeNanoseconds)
}