import kotlin.math.max

private data class Game(val id: Int, val sets: List<Set>) {
    companion object {
        fun parse(s: String): Game {
            val colonSplit = s.split(':', limit = 2)
            val gameId = colonSplit.first().takeLastWhile { it.isDigit() }.toInt()
            val setStrings = colonSplit.last().split(';')

            return Game(gameId, setStrings.map(Set::parse))
        }
    }

    val isPossible get() = sets.all(Set::isPossible)

    val minimumSetOfCubes
        get(): Set {
            var minimumRed = 0
            var minimumGreen = 0
            var minimumBlue = 0
            for (set in sets) {
                if (minimumRed < set.red)
                    minimumRed = set.red
                if (minimumGreen < set.green)
                    minimumGreen = set.green
                if (minimumBlue < set.blue)
                    minimumBlue = set.blue
            }

            return Set(minimumRed, minimumGreen, minimumBlue)
        }
}

private data class Set(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun parse(s: String): Set {
            val cubeStrings = s.split(',').map(String::trim)
            var red = 0
            var green = 0
            var blue = 0
            for (cubeString in cubeStrings) {
                when {
                    cubeString.endsWith("red") -> {
                        red += cubeString.takeWhile { it.isDigit() }.toInt()
                    }

                    cubeString.endsWith("green") -> {
                        green += cubeString.takeWhile { it.isDigit() }.toInt()
                    }

                    cubeString.endsWith("blue") -> {
                        blue += cubeString.takeWhile { it.isDigit() }.toInt()
                    }

                    else -> throw Exception("unknown color in cube string: $cubeString")
                }
            }

            return Set(red, green, blue)
        }
    }

    val isPossible get() = red <= 12 && green <= 13 && blue <= 14
}

fun main() {
    fun part1(input: List<String>): Int {
        val games = input.map(Game::parse)
        return games.filter(Game::isPossible).sumOf(Game::id)
    }

    fun part2(input: List<String>): Int {
        val games = input.map(Game::parse)
        return games.map(Game::minimumSetOfCubes).sumOf { max(1, it.red) * max(1, it.green) * max(1, it.blue) }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
