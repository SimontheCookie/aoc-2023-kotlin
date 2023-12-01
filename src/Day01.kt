fun main() {
    fun part1(input: List<String>): Int {
        fun String.firstInt() = first { it.isDigit() }.digitToInt()
        fun String.lastInt() = last { it.isDigit() }.digitToInt()

        return input.sumOf { (10 * it.firstInt()) + it.lastInt() }
    }

    fun part2(input: List<String>): Int {
        val spelledDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val digitLikePattern = """(${spelledDigits.joinToString("|")}|\d)""".toPattern()
        fun String.toIntIncludingSpelledDigits() = toIntOrNull() ?: (spelledDigits.indexOf(this) + 1)

        return input.sumOf {
            digitLikePattern
                .findAllIncludeOverlapping(it)
                .let { results ->
                    (10 * results.first().toIntIncludingSpelledDigits()) + results.last().toIntIncludingSpelledDigits()
                }
        }
    }

    val test1Input = readInput("Day01_test1")
    check(part1(test1Input) == 142)
    val test2Input = readInput("Day01_test2")
    check(part2(test2Input) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
