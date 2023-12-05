package solver

import Day
import kotlin.math.min

@Day(2023, 5)
class Day5Solver : BaseSolver(), ISolver {
    class Mapper(val to: String) {
        private val maps = mutableMapOf<LongRange, Long>()

        fun addMap(range: LongRange, initial: Long) {
            maps[range] = initial
        }

        fun getMappedValue(input: Long): Long {
            val key = maps.keys.find { input in it }
            if (key != null) {
                return getValue(key, input)
            }

            return input
        }

        private fun getValue(key: LongRange, input: Long): Long {
            return maps[key]!! + input - key.first
        }

        fun getMappedValueExtra(input: List<LongRange>): List<LongRange> {
            val inp = input.toMutableList()
            val result = mutableListOf<LongRange>()
            while (inp.size > 0) {
                val ran = inp.removeFirst()
                val key = maps.keys.find { ran.first in it }
                if (key != null) {
                    val max = min(key.last, ran.last)
                    if (max < ran.last) {
                        val nInp = LongRange(max + 1, ran.last)
                        val nRes = LongRange(getValue(key, ran.first), getValue(key, max))
                        result.add(nRes)
                        inp.add(nInp)
                    } else {
                        result.add(LongRange(getValue(key, ran.first), getValue(key, ran.last)))
                    }
                } else {
                    val k = maps.keys.sortedBy { it.first }.firstOrNull { it.first > ran.first }
                    if (k == null || k.first >= ran.last) {
                        result.add(ran)
                    } else {
                        val nInp = LongRange(k.first, ran.last)
                        val nRes = LongRange(ran.first, k.first - 1)
                        result.add(nRes)
                        inp.add(nInp)
                    }
                }
            }
            return result
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val seeds = lines.first().split(":").last().split(" ").mapNotNull { it.toLongOrNull() }
        val mappers = mutableMapOf<String, Mapper>()

        var currentType = ""

        for (line in lines.subList(2, lines.lastIndex)) {
            if (line.isBlank()) {
                continue
            }

            val matches = """(?<from>[^-\s]*)-to-(?<to>[^\s]*)\smap:""".toRegex().matchEntire(line)

            if (matches != null) {
                currentType = matches.groups["from"]!!.value
                mappers[currentType] = Mapper(matches.groups["to"]!!.value)
            } else {
                val groups = """(?<init>\d+)\s(?<range>\d+)\s(?<count>\d+)""".toRegex().matchEntire(line)!!.groups
                val init = groups["init"]!!.value.toLong()
                val count = groups["count"]!!.value.toLong()
                val range = groups["range"]!!.value.toLong()

                mappers[currentType]!!.addMap(LongRange(range, range + count - 1), init)
            }
        }

        val seedToLocation = mutableMapOf<Long, Long>()

        for (seed in seeds) {
            var from = "seed"
            var input = seed

            while (true) {
                input = mappers[from]!!.getMappedValue(input)
                from = mappers[from]!!.to

                if (from == "location") {
                    break
                }
            }

            seedToLocation[seed] = input
        }

        val part1 = seedToLocation.values.min().toString()

        var from = "seed"
        var input = seeds.chunked(2).map { LongRange(it[0], it[0] + it[1]) }

        while (true) {
            val mapper = mappers[from]!!
            input = mapper.getMappedValueExtra(input)
            from = mapper.to

            if (from == "location") {
                break
            }
        }

        return Pair(part1, input.minOf { it.first }.toString())
    }
}