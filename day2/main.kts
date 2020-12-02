import java.io.File
import kotlin.system.exitProcess


data class Rule(val min: Int, val max: Int, val ch: Char)
val to_rule : (String) -> Rule = { s ->
    val t1 = s.split(" ")
    val t2 = t1[0].split("-")
    Rule(t2[0].toInt(), t2[1].toInt(), t1[1][0])
}

val count_occ : (String, Char) -> Int = { s, ch -> s.filter { c -> c ==  ch }.count() }
val between : (Int, Int, Int) -> Boolean = { x, a, b  -> a <= x && x <= b }

val lines = File("input").readLines().map { it.split(":") }
val rules = lines.map { to_rule(it[0]) }
val passwords = lines.map { it[1].trim() }

val result_one = rules.zip(passwords)
        .filter {  between(count_occ(it.second, it.first.ch), it.first.min, it.first.max) }
        .count()

// ----- Part Two -----

val has_char: (String, Char, Int) -> Boolean = { s, ch, pos -> pos <= s.length && s[pos - 1] == ch }
val result_two = rules.zip(passwords)
        .filter { has_char(it.second, it.first.ch, it.first.min) xor has_char(it.second, it.first.ch, it.first.max) }
        .count()

println("Result two: " + result_two)