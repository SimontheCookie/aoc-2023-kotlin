import java.util.*

private fun List<Long>.filterNotInList() = toSet().let { (it.min()..it.max()).filter { i -> i !in it }.toSet() }

private class Image(input: List<String>) {
    val galaxies: List<Galaxy>
    val emptyRows: Set<Long>
    val emptyColumns: Set<Long>

    init {
        galaxies = buildList {
            for ((y, line) in input.withIndex()) {
                for ((x, c) in line.withIndex()) {
                    if (c == '#')
                        add(Galaxy(x.toLong(), y.toLong()))
                }
            }
        }.sortedWith(Comparator.comparingLong<Galaxy> { it.y }.thenComparingLong { it.x })

        emptyRows = galaxies.map(Galaxy::x).filterNotInList()
        emptyColumns = galaxies.map(Galaxy::y).filterNotInList()
    }

    fun combinations() = buildSet {
        for (a in galaxies) {
            for (b in galaxies) {
                if (a != b && !contains(Pair(b, a)))
                    add(Pair(a, b))
            }
        }
    }.toList()

    fun sumOfAllDistances(emptySpaceFactor: Long) =
        combinations().sumOf { it.first.distance(it.second, emptySpaceFactor) }

    inner class Galaxy(val x: Long, val y: Long) {
        fun distance(other: Galaxy, emptySpaceFactor: Long): Long {
            val x = (x.coerceAtMost(other.x).inc()..x.coerceAtLeast(other.x)).sumOf { i ->
                if (i in emptyRows) emptySpaceFactor else 1
            }
            val y = (y.coerceAtMost(other.y).inc()..y.coerceAtLeast(other.y)).sumOf { i ->
                if (i in emptyColumns) emptySpaceFactor else 1
            }
            return x + y
        }

        override fun toString(): String {
            return "($x, $y)"
        }

        override fun equals(other: Any?): Boolean {
            if (other is Galaxy) return x == other.x && y == other.y
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return Objects.hash(x, y)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return Image(input).sumOfAllDistances(2L)
    }

    fun part2(input: List<String>, factor: Long = 1000000L): Long {
        return Image(input).sumOfAllDistances(factor)
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 10L) == 1030L)
    check(part2(testInput, 100L) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
