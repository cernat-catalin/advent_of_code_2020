import java.io.File

val content = File("input").readText()
val passports = content.split("\n\n")

val passport_keys = passports.map { it.split("\\s+".toRegex()).map { it.split(":")[0] } }
val mandatory_fields = listOf<String>("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
val result_one = passport_keys.filter { it.containsAll(mandatory_fields) }.count()
println("Result one: " + result_one)


// ----- Part Two -----

val valid_values: (String, String) -> Boolean = { field, value ->

    val is_number: (String) -> Boolean = { s -> s.toIntOrNull() != null }

    val between: (String, Int, Int) -> Boolean = { s, a, b ->
        val x = s.toIntOrNull()
        if (x == null) false else a <= x && x <= b
    }

    val valid_height: (String) -> Boolean = { s ->
        if (s.length < 2) false
        else {
            val measure = s.takeLast(2)
            val height = s.take(s.length - 2)
            when (measure) {
                "in" -> between(height, 59, 76)
                "cm" -> between(height, 150, 193)
                else -> false
            }
        }
    }

    when (field) {
        "byr" -> between(value, 1920, 2002)
        "iyr" -> between(value, 2010, 2020)
        "eyr" -> between(value, 2020, 2030)
        "hgt" -> valid_height(value)
        "hcl" -> value.matches("#[a-f0-9]{6}".toRegex())
        "ecl" -> value in listOf<String>("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        "pid" -> value.length == 9 && is_number(value)
        "cid" -> true
        else -> false
    }
}

val passport_kv = passports.map { it.split("\\s+".toRegex()).map { it.split(":") } }
val result_two = passport_kv.filter { it.map { it[0] }.containsAll(mandatory_fields) }
        .filter { it.all { kv -> valid_values(kv[0], kv[1]) } }
        .count()
println("Result two: " + result_two)
