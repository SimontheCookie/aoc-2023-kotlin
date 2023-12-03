import kotlin.math.max
import kotlin.math.min

private val numberRegex = """\d+""".toRegex()

private fun CharSequence.hasAnySymbol() = any { it != '.' && !it.isDigit() }

private fun isPartNumber(lines: List<String>, lineIndex: Int, positionRange: IntRange): Boolean {
    val currentLine = lines[lineIndex]

    // left or right is a symbol
    if (currentLine.getOrElse(positionRange.first - 1) { '.' } != '.'
        || currentLine.getOrElse(positionRange.last + 1) { '.' } != '.')
        return true

    val rangeWithDiagonal =
        IntRange(max(0, positionRange.first - 1), min(currentLine.length - 1, positionRange.last + 1))

    // top is any symbol
    if (lineIndex > 0 && lines[lineIndex - 1].substring(rangeWithDiagonal).hasAnySymbol())
        return true

    // bottom is any symbol
    if ((lineIndex + 1) < lines.size && lines[lineIndex + 1].substring(rangeWithDiagonal).hasAnySymbol())
        return true

    return false
}

fun main() {
    fun part1(input: List<String>): Int {
        var result = 0
        for ((lineIndex, line) in input.withIndex()) {
            for (matchResult in numberRegex.findAll(line)) {
                if (isPartNumber(input, lineIndex, matchResult.range))
                    result += matchResult.value.toInt()
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        fun findAdjacentPartNumbers(lineIndex: Int, gearPosition: Int): List<Int> {
            val currentLine = input[lineIndex]

            return buildList {
                // left
                if (gearPosition > 0 && currentLine[gearPosition - 1].isDigit()) {
                    val numberRange =
                        IntRange(currentLine.take(gearPosition - 1).indexOfLast { !it.isDigit() } + 1, gearPosition - 1)
                    if (isPartNumber(input, lineIndex, numberRange))
                        add(currentLine.substring(numberRange).toInt())
                }

                // right
                if (gearPosition + 1 < currentLine.length && currentLine[gearPosition + 1].isDigit()) {
                    val numberRange =
                        IntRange(
                            gearPosition + 1,
                            currentLine.drop(gearPosition + 1).indexOfFirst { !it.isDigit() } + gearPosition)
                    if (isPartNumber(input, lineIndex, numberRange))
                        add(currentLine.substring(numberRange).toInt())
                }

                val rangeWithDiagonal =
                    IntRange(max(0, gearPosition - 1), min(input[lineIndex].length - 1, gearPosition + 1))

                // top
                if (lineIndex > 0) {
                    val prevLine = input[lineIndex - 1]
                    if (prevLine.substring(rangeWithDiagonal).any { it.isDigit() })
                        addAll(
                            numberRegex
                                .findAll(prevLine)
                                .filter { isPartNumber(input, lineIndex - 1, it.range) }
                                .filter { rangeWithDiagonal.contains(it.range.first) || rangeWithDiagonal.contains(it.range.last) }
                                .map { it.value.toInt() }
                        )
                }

                // bottom
                if ((lineIndex + 1) < input.size) {
                    val nextLine = input[lineIndex + 1]
                    if (nextLine.substring(rangeWithDiagonal).any { it.isDigit() })
                        addAll(
                            numberRegex
                                .findAll(nextLine)
                                .filter { isPartNumber(input, lineIndex + 1, it.range) }
                                .filter { rangeWithDiagonal.contains(it.range.first) || rangeWithDiagonal.contains(it.range.last) }
                                .map { it.value.toInt() }
                        )
                }
            }
        }

        var result = 0
        val gearRegex = """\*""".toRegex()
        for ((lineIndex, line) in input.withIndex()) {
            for (matchResult in gearRegex.findAll(line)) {
                val adjacentPartNumbers = findAdjacentPartNumbers(lineIndex, matchResult.range.first)
                if (adjacentPartNumbers.size == 2)
                    result += adjacentPartNumbers.first() * adjacentPartNumbers.last()
            }
        }

        return result
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
