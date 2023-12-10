private typealias Tile = Pair<Int, Int>

private enum class Direction(val yDelta: Int, val xDelta: Int) {
    North(-1, 0),
    East(0, 1),
    South(1, 0),
    West(0, -1)
}

private operator fun Tile.plus(other: Direction): Tile = Pair(first + other.yDelta, second + other.xDelta)

private enum class Label(val directions: Set<Direction> = setOf()) {
    V(setOf(Direction.North, Direction.South)),
    H(setOf(Direction.East, Direction.West)),
    NE(setOf(Direction.North, Direction.East)),
    NW(setOf(Direction.North, Direction.West)),
    SE(setOf(Direction.South, Direction.East)),
    SW(setOf(Direction.South, Direction.West)),
    A(setOf(Direction.North, Direction.East, Direction.South, Direction.West)),
    G();

    fun canConnect(o: Label, d: Direction) = when (d) {
        Direction.North -> o.directions.contains(Direction.South)
        Direction.East -> o.directions.contains(Direction.West)
        Direction.South -> o.directions.contains(Direction.North)
        Direction.West -> o.directions.contains(Direction.East)
    }
}

private val Char.label
    get() = when (this) {
        '|' -> Label.V
        '-' -> Label.H
        'L' -> Label.NE
        'J' -> Label.NW
        'F' -> Label.SE
        '7' -> Label.SW
        'S' -> Label.A
        else -> Label.G
    }

private class Network(val map: List<String>) {
    fun Tile.label() = (map.getOrNull(first)?.getOrNull(second) ?: '.').label

    fun Tile.allConnectingTiles() = Direction.entries
        .filter { label().canConnect((this + it).label(), it) }
        .map { this + it }

    fun Tile.nextTile(prev: Tile): Tile = label().directions.first { this + it != prev }.let { this + it }

    fun trackPipe(start: Tile): Int {
        var steps = 1
        var current = start.allConnectingTiles().first()
        var previous = start
        while (current != start) {
            val next = current.nextTile(previous)
            previous = current
            current = next
            steps++
        }
        return steps
    }

    fun findStartingTile(): Tile {
        for ((y, l) in map.withIndex()) {
            for ((x, c) in l.withIndex()) {
                if (c == 'S')
                    return Pair(y, x)
            }
        }
        throw IllegalStateException()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val network = Network(input)
        return network.trackPipe(network.findStartingTile()) / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    check(part1(readInput("Day10_test1")) == 8)
    check(part2(readInput("Day10_test2")) == 10)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
