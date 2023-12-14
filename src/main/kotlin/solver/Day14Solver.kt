package solver

import Day
import kotlin.math.max
import kotlin.math.min


@Day(2023, 14)
class Day14Solver : BaseSolver(), ISolver {
    enum class Direction {
        North,
        South,
        West,
        East
    }

    private fun tilt(
        staticRocks: List<Pair<Int, Int>>,
        movingRocks: List<Pair<Int, Int>>,
        maxY: Int,
        maxX: Int,
        direction: Direction
    ): MutableList<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        when (direction) {
            Direction.North -> {
                for (y in 0..maxY) {
                    for (moving in movingRocks.filter { it.second == y }) {
                        val m = max(
                            staticRocks.sortedBy { it.second }
                                .first { it.first == moving.first && it.second < y }.second
                                ?: -1,
                            result.filter { it.first == moving.first && it.second < y }.maxOfOrNull { it.second } ?: -1
                        )
                        result.add(Pair(moving.first, m + 1))
                    }
                }
            }

            Direction.South -> {
                for (y in 0..maxY) {
                    for (moving in movingRocks.filter { it.second == maxY - y }) {
                        val m = min(
                            staticRocks.filter { it.first == moving.first && it.second > maxY - y }
                                .minOfOrNull { it.second }
                                ?: (maxY + 1),
                            result.filter { it.first == moving.first && it.second > maxY - y }.minOfOrNull { it.second }
                                ?: (maxY + 1)
                        )
                        result.add(Pair(moving.first, m - 1))
                    }
                }
            }

            Direction.West -> {
                for (x in 0..maxX) {
                    for (moving in movingRocks.filter { it.first == x }) {
                        val m = max(
                            staticRocks.filter { it.second == moving.second && it.first < x }.maxOfOrNull { it.first }
                                ?: -1,
                            result.filter { it.second == moving.second && it.first < x }.maxOfOrNull { it.first }
                                ?: -1
                        )
                        result.add(Pair(m + 1, moving.second))
                    }
                }
            }

            Direction.East -> {
                for (x in 0..maxX) {
                    for (moving in movingRocks.filter { it.first == maxX - x }) {
                        val m = min(
                            staticRocks.filter { it.second == moving.second && it.first > maxX - x }
                                .minOfOrNull { it.first }
                                ?: (maxX + 1),
                            result.filter { it.second == moving.second && it.first > maxX - x }.minOfOrNull { it.first }
                                ?: (maxX + 1)
                        )
                        result.add(Pair(m - 1, moving.second))
                    }
                }
            }
        }

        return result

    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)
        val staticRocks = mutableListOf<Pair<Int, Int>>()
        var movingRocks = mutableListOf<Pair<Int, Int>>()

        for (y in 0..lines.lastIndex) {
            if (lines[y].isBlank()) {
                continue
            }
            for (x in 0..lines[y].lastIndex) {
                when (lines[y][x]) {
                    '#' -> staticRocks.add(Pair(x, y))
                    'O' -> movingRocks.add(Pair(x, y))
                }
            }
        }

        val original = mutableListOf<List<Pair<Int, Int>>>()

        val result = tilt(staticRocks, movingRocks, lines.lastIndex - 1, lines[0].lastIndex, Direction.North)
        val part1 = result.sumOf { lines.size - it.second - 1 }
        var part2 = 0

        for (i in 0..<1000000000) {
            movingRocks = tilt(staticRocks, movingRocks, lines.lastIndex - 1, lines[0].lastIndex, Direction.North)
            movingRocks = tilt(staticRocks, movingRocks, lines.lastIndex - 1, lines[0].lastIndex, Direction.West)
            movingRocks = tilt(staticRocks, movingRocks, lines.lastIndex - 1, lines[0].lastIndex, Direction.South)
            movingRocks = tilt(staticRocks, movingRocks, lines.lastIndex - 1, lines[0].lastIndex, Direction.East)

            val copy = movingRocks.toList()

            val index = original.indexOf(copy)
            if (index != -1) {
                val extra = (1000000000 - (index + 1)) % (i - index)
                part2 = original[index + extra].sumOf { lines.size - it.second - 1 }
                break
            }

            original.add(copy)
        }

        return Pair(part1.toString(), part2.toString())
    }
}