// WITH_RUNTIME
fun interface I<A, B, C, D> {
    fun method(x: Pair<A, B>): Pair<C, D>
}

fun main() {
    val x = <caret>I { x: Pair<Int, String> -> Pair(x.first.toLong(), x.second.toInt()) }
}
