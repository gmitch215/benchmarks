package xyz.gmitch215.benchmarks

operator fun <T> List<T>.times(n: Int): List<T> {
    val result = mutableListOf<T>()
    repeat(n) {
        result.addAll(this)
    }

    return result
}

fun <T> Collection<T>.repeat(times: Int): List<T> {
    val result = mutableListOf<T>()
    for (t in this) {
        result.addAll(List(times) { t })
    }

    return result
}