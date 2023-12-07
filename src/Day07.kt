private enum class Type(val r: Int) {
    HighCard(0),
    OnePair(1),
    TwoPair(2),
    ThreeOfAKind(3),
    FullHouse(4),
    FourOfAKind(5),
    FiveOfAKind(6),
}

private fun getTypeOfHand(hand: String, allowJoker: Boolean): Type {
    val map = buildMap<Char, Int> {
        for (c in hand) {
            if (!containsKey(c))
                put(c, 0)
            this[c] = this[c]!! + 1
        }
    }

    if (allowJoker) {
        val countJokers = map['J'] ?: 0
        val countDifferentChars = map.keys.size
        if (countDifferentChars == 1 || (countDifferentChars == 2 && countJokers > 0))
            return Type.FiveOfAKind
        if (map.values.any { it == 4 } || map.any { it.key != 'J' && it.value + countJokers >= 4 })
            return Type.FourOfAKind
        if (countDifferentChars == 2 || (countDifferentChars == 3 && countJokers > 0))
            return Type.FullHouse
        if ((countDifferentChars == 3 && map.values.any { it == 3 }) || (countDifferentChars == 4 && map.any { it.key != 'J' && it.value + countJokers >= 3 }))
            return Type.ThreeOfAKind
        if ((countDifferentChars == 3 && map.values.count { it == 2 } == 2) || (countJokers > 0 && map.values.any { it == 2 }))
            return Type.TwoPair
        if (countDifferentChars == 4 || countJokers > 0)
            return Type.OnePair
    } else {
        val countDifferentChars = map.keys.size
        if (countDifferentChars == 1)
            return Type.FiveOfAKind
        if (map.values.any { it == 4 })
            return Type.FourOfAKind
        if (countDifferentChars == 2)
            return Type.FullHouse
        if (countDifferentChars == 3 && map.values.any { it == 3 })
            return Type.ThreeOfAKind
        if (countDifferentChars == 3 && map.values.count { it == 2 } == 2)
            return Type.TwoPair
        if (countDifferentChars == 4)
            return Type.OnePair
    }

    return Type.HighCard
}

private class HandComparator : Comparator<Pair<String, Long>> {
    override fun compare(o1: Pair<String, Long>?, o2: Pair<String, Long>?): Int {
        fun foo(hand: String) = hand
            .replace('A', 'Z')
            .replace('K', 'Y')
            .replace('Q', 'X')
            .replace('J', 'V')
            .replace('T', 'U')

        if (o1 == null)
            return if (o2 == null) 0 else -1
        if (o2 == null)
            return 1
        val left = foo(o1.first)
        val right = foo(o2.first)
        return left.compareTo(right)
    }
}

private class HandComparatorWithJoker : Comparator<Pair<String, Long>> {
    override fun compare(o1: Pair<String, Long>?, o2: Pair<String, Long>?): Int {
        fun foo(hand: String) = hand
            .replace('A', 'Z')
            .replace('K', 'Y')
            .replace('Q', 'X')
            .replace('J', '1')
            .replace('T', 'U')

        if (o1 == null)
            return if (o2 == null) 0 else -1
        if (o2 == null)
            return 1
        val left = foo(o1.first)
        val right = foo(o2.first)
        return left.compareTo(right)
    }
}

private fun splitHandsByTypeSortedByCardQuality(
    input: List<String>,
    allowJoker: Boolean
): Map<Type, MutableList<Pair<String, Long>>> {
    return buildMap {
        for (line in input) {
            val (hand, bidStr) = line.split(' ', limit = 2)
            val type = getTypeOfHand(hand, allowJoker)
            if (!containsKey(type))
                put(type, mutableListOf())
            this[type]!!.add(Pair(hand, bidStr.toLong()))
        }

        values.forEach { list -> list.sortWith(if (allowJoker) HandComparatorWithJoker() else HandComparator()) }
    }
}

private fun getTotalWinnings(map: Map<Type, MutableList<Pair<String, Long>>>): Long {
    var result = 0L
    var currentRank = 0
    for (pair in map.keys.sortedBy { it.r }.flatMap { map[it]!! }) {
        result += pair.second * ++currentRank
    }
    return result
}

fun main() {
    fun part1(input: List<String>): Long {
        val handsByTypeMap = splitHandsByTypeSortedByCardQuality(input, false)
        return getTotalWinnings(handsByTypeMap)
    }

    fun part2(input: List<String>): Long {
        val handsByTypeMap = splitHandsByTypeSortedByCardQuality(input, true)
        return getTotalWinnings(handsByTypeMap)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
