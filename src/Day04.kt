private class Card(val id: Int, val winningNumbers: Set<Int>, val myNumbers: Set<Int>) {
    companion object {
        fun parse(s: String): Card {
            val colonIndex = s.indexOf(':')
            val id = s.take(colonIndex).takeLastWhile { it.isDigit() }.toInt()
            val numberParts = s.drop(colonIndex + 1).split('|', limit = 2)
            val winningNumbers = numberParts[0]
                .split(' ')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            val myNumbers = numberParts[1]
                .split(' ')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toInt() }

            return Card(id, winningNumbers.toHashSet(), myNumbers.toHashSet())
        }
    }

    val countMatchingNumbers by lazy { myNumbers.count { winningNumbers.contains(it) } }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map(Card::parse)
            .sumOf {
                when (val countMatchingNumbers = it.countMatchingNumbers) {
                    0 -> 0
                    else -> {
                        var result = 1
                        for (i in 1..<countMatchingNumbers) {
                            result += result
                        }
                        result
                    }
                }
            }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map(Card::parse).associateBy { it.id }
        val countCards = cards.mapValues { 1 }.toMutableMap()
        var x = 0
        while (true) {
            val l = countCards.filter { it.value > x }
            if (l.isEmpty())
                break
            for (i in l) {
                val card = cards.getOrElse(i.key) { throw IllegalStateException() }
                val countMatchingNumbers = card.countMatchingNumbers
                if (countMatchingNumbers > 0)
                    ((card.id + 1).coerceAtMost(cards.size)..(card.id + countMatchingNumbers).coerceAtMost(cards.size))
                        .forEach { countCards[it] = countCards[it]!! + 1 }
            }
            x++
        }
        return countCards.values.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
