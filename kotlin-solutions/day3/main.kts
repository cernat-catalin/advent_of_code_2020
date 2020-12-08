import java.io.File


val lines = File("input").readLines()
val n = lines.size
val m = lines[0].length

val go_down_step: (Pair<Int, Int>, Pair<Int, Int>, Int) -> Pair<Pair<Int, Int>, Int> = { (x, y), (dx, dy), a ->
    val add_a = if (lines[x + dx][(y + dy) % m] == '#') 1 else 0
    Pair(Pair(x + dx, y + dy), a + add_a)
}

fun go_down(pos: Pair<Int, Int>, slope: Pair<Int, Int>, tree: Int): Pair<Pair<Int, Int>, Int> {
    return (if (pos.first + slope.first < n) {
        val (npos, ntree) = go_down_step(pos, slope, tree)
        go_down(npos, slope, ntree)
    } else Pair(pos, tree))
}

val result_one = go_down(Pair(0, 0), Pair(1, 3), 0).second
println("Result one: " + result_one)


// ----- Part Two -----

val result_two = listOf<Pair<Int, Int>>(Pair(1, 1), Pair(1, 3), Pair(1, 5), Pair(1, 7), Pair(2, 1))
        .map { go_down(Pair(0, 0), it, 0).second }
        .fold(1L) { acc, x -> acc * x }

println("Result two: " + result_two)