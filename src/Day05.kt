private class Almanac(val seeds: List<Long>, val conversions: List<Conversion>) {
    companion object {
        fun parse(input: List<String>): Almanac {
            fun findEmptyLineIndex(fromIndex: Int): Int {
                for (i in fromIndex..<input.size)
                    if (input[i].isEmpty())
                        return i
                return input.size
            }

            val seeds = """\d+""".toRegex().findAll(input.first()).map { it.value.toLong() }.toList()
            val conversions = buildList {
                var emptyLineIndex = findEmptyLineIndex(1)
                while (emptyLineIndex < input.size) {
                    val nextEmptyLineIndex = findEmptyLineIndex(emptyLineIndex + 1)
                    if (nextEmptyLineIndex != emptyLineIndex + 1)
                        add(
                            Conversion.parse(
                                input.subList(emptyLineIndex + 1, nextEmptyLineIndex),
                                nextEmptyLineIndex >= input.size
                            )
                        )
                    emptyLineIndex = nextEmptyLineIndex
                }
            }

            return Almanac(seeds, conversions)
        }
    }

    fun convertAll(input: Long) = conversions.fold(input) { acc, conversion -> conversion.map(acc) }
}

private class Conversion(val name: String, val mappings: List<ConversionMapping>) {
    companion object {
        fun parse(input: List<String>, sort: Boolean): Conversion {
            val name = input.first().trimEnd(':')
            var mappings = input.subList(1, input.size).map(ConversionMapping::parse)
            if (sort)
                mappings = mappings.sortedBy { it.destinationStart }
            return Conversion(name, mappings)
        }
    }

    fun map(input: Long) = mappings.firstOrNull { it.sourceRange.contains(input) }?.map(input) ?: input
}

private class ConversionMapping(val sourceRange: LongRange, val destinationStart: Long) {
    companion object {
        fun parse(s: String): ConversionMapping {
            val parts = s.split(' ').map(String::trim).filter(String::isNotEmpty).map(String::toLong)
            return ConversionMapping(parts[1]..<(parts[1] + parts[2]), parts[0])
        }
    }

    fun map(input: Long) = if (sourceRange.contains(input)) destinationStart + (input - sourceRange.first) else input
}

fun main() {
    fun part1(input: List<String>): Long {
        val almanac = Almanac.parse(input)
        return almanac.seeds.minOf { almanac.convertAll(it) }
    }

    fun part2(input: List<String>): Long {
        val almanac = Almanac.parse(input)
        return almanac.seeds
            .chunked(2)
            .parallelStream()
            .map {
                var localMin = Long.MAX_VALUE
                for (s in it[0].rangeUntil(it[0] + it[1])) {
                    val l = almanac.convertAll(s)
                    if (l < localMin)
                        localMin = l
                }
                return@map localMin
            }
            .min(Long::compareTo)
            .get()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
