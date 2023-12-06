import kotlin.math.max

private fun canWin(timeHolding: Long, timeTotal: Long, distance: Long): Boolean {
    val timeToMove = max(0, timeTotal - timeHolding)
    return timeToMove * timeHolding > distance
}

private fun calculateWaysToWin(races: List<Pair<Long, Long>>): Long {
    var result = 1L
    for ((time, distance) in races) {
        val r = 0..time
        val minimum = r.first { canWin(it, time, distance) }
        val maximum = r.last { canWin(it, time, distance) }
        result *= maximum - minimum + 1
    }
    return result
}

fun main() {
    fun part1(input: List<String>): Long {
        val numberRegex = """\d+""".toRegex()
        val times = numberRegex.findAll(input[0]).map { it.value.toLong() }
        val distances = numberRegex.findAll(input[1]).map { it.value.toLong() }.toList()
        val races = times.mapIndexed { index, i -> Pair(i, distances[index]) }.toList()

        return calculateWaysToWin(races)
    }

    fun part2(input: List<String>): Long {
        val numberRegex = """\d+""".toRegex()
        val time = numberRegex.findAll(input[0]).joinToString(separator = "") { it.value }.toLong()
        val distance = numberRegex.findAll(input[1]).joinToString(separator = "") { it.value }.toLong()
        val races = listOf(Pair(time, distance))

        return calculateWaysToWin(races)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
