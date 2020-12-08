import java.io.File
import java.util.*


fun mark_occ_or(answers: List<String>): BooleanArray {
    val occ = BooleanArray(26) { false }
    answers.forEach{ it.forEach { occ[it - 'a'] = true } }
    return occ
}

fun mark_occ_and(answers: List<String>): BooleanArray {
    val occ = IntArray(26) { 0 }
    answers.forEach{ it.forEach { occ[it - 'a'] += 1 } }
    return occ.map { it == answers.size }.toBooleanArray()
}

val content = File("input").readText()
val groups = content.split("\n\n").map { it.split("\n") }

val result_one = groups.map { mark_occ_or(it) }
        .map { it.sumBy { if (it) 1 else 0 } }
        .sum()
println("Result one: " + result_one)

// ----- Part Two -----
val result_two = groups.map { mark_occ_and(it) }
        .map { it.sumBy { if (it) 1 else 0 } }
        .sum()
println("Result two: " + result_two)
