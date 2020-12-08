import java.io.File
import kotlin.system.exitProcess

fun <S, T> List<S>.cartesianProduct(other: List<T>) = this.flatMap {
    List(other.size){ i -> Pair(it, other[i]) }
}

val entries = File("day1/input").readLines().map { it.toInt() }

val result_one = entries.cartesianProduct(entries)
        .filter { (x, y) -> x + y == 2020 }
        .map { (x, y) -> x * y }
        .first()
println("Part one: " + result_one)

// ----- Part Two -----

val result_two = entries
        .map { Pair(it, entries.cartesianProduct(entries).filter { (x, y) -> it + x + y == 2020 }.firstOrNull()) }
        .filter { it.second != null }
        .map { p -> p.first * (p.second?.first ?: 0) * (p.second?.second ?: 0) }
        .first()
println("Part two: " + result_two)
