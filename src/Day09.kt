private fun parse(input: List<String>) = input.map { it.split(' ').map(String::toInt).toIntArray() }

private fun getIntermediates(history: IntArray) = buildList {
    var current = history
    add(current)
    do {
        check(current.size > 1)
        val next = IntArray(current.size - 1)
        for (i in 1..<current.size)
            next[i - 1] = current[i] - current[i - 1]
        current = next
        if (current.all { it == 0 })
            break
        add(current)
    } while (true)
}

fun main() {
    fun part1(input: List<String>): Int {
        fun findNextValue(history: IntArray): Int {
            val intermediates = getIntermediates(history)

            var nextValue = 0
            for (intermediate in intermediates.reversed()) {
                nextValue += intermediate.last()
            }
            return nextValue
        }

        return parse(input).sumOf { findNextValue(it) }
    }

    fun part2(input: List<String>): Int {
        fun findNextValue(history: IntArray): Int {
            val intermediates = getIntermediates(history)

            var nextValue = 0
            for (intermediate in intermediates.reversed()) {
                nextValue = intermediate.first() - nextValue
            }
            return nextValue
        }

        return parse(input).sumOf { findNextValue(it) }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
