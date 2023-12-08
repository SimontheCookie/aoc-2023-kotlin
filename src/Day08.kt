private fun parseInstructions(input: List<String>) = input[0].map {
    when (it) {
        'L' -> 0
        'R' -> 1
        else -> throw IllegalStateException()
    }
}

private val r = """\((\w+), (\w+)\)""".toRegex()

private fun parseNetwork(input: List<String>): Map<String, Pair<String, String>> = input
    .drop(2)
    .associateBy({ it.substringBefore(' ') }) {
        r.findAll(it).first().let { match -> Pair(match.groupValues[1], match.groupValues[2]) }
    }

private operator fun Pair<String, String>.get(i: Int) = when (i) {
    0 -> first
    1 -> second
    else -> throw IllegalArgumentException()
}

private fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private fun findLCM(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1..<numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = parseInstructions(input)
        val network = parseNetwork(input)
        var steps = 0
        var currentNode = "AAA"
        while (currentNode != "ZZZ") {
            var iI = 0
            while (iI < instructions.size && currentNode != "ZZZ") {
                currentNode = network[currentNode]!![instructions[iI++]]
                steps++
            }
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = parseInstructions(input)
        val network = parseNetwork(input)
        val steps = network.keys
            .filter { it.endsWith('A') }
            .map {
                var step = 0L
                var currentNode = it
                while (!currentNode.endsWith('Z')) {
                    var iI = 0
                    while (iI < instructions.size && !currentNode.endsWith('Z')) {
                        currentNode = network[currentNode]!![instructions[iI++]]
                        step++
                    }
                }
                step
            }
        return findLCM(steps)
    }

    val testInput = readInput("Day08_test1")
    check(part1(testInput) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
