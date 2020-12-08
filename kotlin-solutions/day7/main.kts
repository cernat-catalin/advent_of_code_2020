import java.io.File

fun dfs(x: Int, visited: BooleanArray, graph: HashMap<Int, MutableList<Int>>): Unit {
    visited[x] = true

    for (y in graph.getOrElse(x, { ArrayList<Int>() })) {
        dfs(y, visited, graph)
    }
}

fun dfs_count(x: Int, graph: HashMap<Int, MutableList<Pair<Int, Int>>>): Long {
    val neightbours = graph.getOrElse(x, { ArrayList<Pair<Int, Int>>() })
    return neightbours.fold(0L) { acc, y -> acc + (dfs_count(y.first, graph) + 1) * y.second }
}

val lines = File("input").readLines()

val graph = HashMap<Int, MutableList<Pair<Int, Int>>>()
val inverted_graph = HashMap<Int, MutableList<Int>>()

val color_mapping = HashMap<String, Int>()
var max_index = 0
val add_color: (String) -> Unit = { s -> color_mapping.put(s, ++max_index) }

for (line in lines) {
    val tokens = line.split(" ")

    if (tokens[4] == "no") // contains no other bags
        continue

    val x = tokens[0] + " " + tokens[1]
    if (!color_mapping.containsKey(x))
        add_color(x)

    for (j in 4..tokens.size - 1 step 4) {
        val y = tokens[j + 1] + " " + tokens[j + 2]
        val n = tokens[j].toInt()

        if (!color_mapping.containsKey(y))
            add_color(y)

        val x_index = color_mapping[x] ?: 0
        val y_index = color_mapping[y] ?: 0

        graph.computeIfAbsent(x_index) { _ -> ArrayList<Pair<Int, Int>>() }
        graph.getOrElse(x_index, { ArrayList<Pair<Int, Int>>() }).add(Pair(y_index, n))

        inverted_graph.computeIfAbsent(y_index) { _ -> ArrayList<Int>() }
        inverted_graph.getOrElse(y_index, { ArrayList<Int>() }).add(x_index)
    }
}

val visited = BooleanArray(max_index + 1) { false }
dfs(color_mapping["shiny gold"] ?: 0, visited, inverted_graph)

val result_one = visited.sumBy { if (it) 1 else 0 } - 1
println("Result one: " + result_one)


//// ----- Part Two -----

//val result_two = dfs_count(color_mapping["shiny gold"] ?: 0, visited_two, graph)
val result_two = dfs_count(color_mapping["shiny gold"] ?: 0, graph)
println("Result two: " + result_two)
