import java.io.File
import java.util.*


// find unit in partition of size 2^(halfs.size) by <halfs> (0: left | 1: right)
fun bin_part(halfs: Queue<Short>): Int {
    val partial_unit = 1 shl (halfs.size - 1)
    halfs.poll()?.let {
        if (it == 0.toShort())
            return bin_part(halfs)
        else
            return partial_unit + bin_part(halfs)
    }

    return 0
}

fun to_binary_halfs(s: String): Queue<Short> {
    return s.map { ch ->
        when (ch) {
            'F', 'L' -> 0.toShort()
            else -> 1.toShort()
        }
    }
            .toCollection(LinkedList<Short>())
}

val lines = File("input").readLines()
val seat_ids = lines.map { Pair(it.substring(0, 7), it.substring(7)) }
        .map { Pair(to_binary_halfs(it.first), to_binary_halfs(it.second)) }
        .map { Pair(bin_part(it.first), bin_part(it.second)) }
        .map { it.first * 8 + it.second }

val result_one = seat_ids.maxOrNull() ?: 0
println("Result one: " + result_one)

// ----- Part Two -----

val min_seat_id = seat_ids.minOrNull() ?: 0
val max_seat_id = seat_ids.maxOrNull() ?: 0
val expected_sum = (max_seat_id * (max_seat_id + 1)) / 2 - (min_seat_id * (min_seat_id - 1)) / 2
val result_two = expected_sum - seat_ids.sum()
println("Result two: " + result_two)
