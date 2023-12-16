package solver

import Day
import kotlin.math.max
import kotlin.math.min

// 10 segundos

@Day(2023, 14)
class Day14Solver : BaseSolver(), ISolver {
    enum class Direction {
        North,
        South,
        West,
        East
    }

    private fun tilt(
        staticRocks: Pair<Map<Int, List<Int>>, Map<Int, List<Int>>>,
        movingRocks: Pair<Map<Int, List<Int>>, Map<Int, List<Int>>>,
        maxY: Int,
        maxX: Int,
        direction: Direction
    ): Pair<Map<Int, List<Int>>, Map<Int, List<Int>>> {
        val result = Pair(
            mutableMapOf<Int, MutableList<Int>>(),
            mutableMapOf<Int, MutableList<Int>>()
        )

        when (direction) {
            Direction.North -> {
                for (y in 0..maxY) {
                    if (!movingRocks.second.containsKey(y)) {
                        continue
                    }

                    for (movingX in movingRocks.second[y]!!) {
                        val m = max(
                            staticRocks.first[movingX]?.filter { it < y }?.maxOrNull() ?: -1,
                            result.first[movingX]?.filter { it < y }?.maxOrNull() ?: -1
                        )

                        addToPairMap(result, movingX, m + 1)
                    }
                }
            }

            Direction.South -> {
                for (y in 0..maxY) {
                    if (!movingRocks.second.containsKey(maxY - y)) {
                        continue
                    }

                    for (movingX in movingRocks.second[maxY - y]!!) {
                        val m = min(
                            staticRocks.first[movingX]?.filter { it > maxY - y }?.minOrNull() ?: (maxY + 1),
                            result.first[movingX]?.filter { it > maxY - y }?.minOrNull() ?: (maxY + 1),
                        )
                        addToPairMap(result, movingX, m - 1)
                    }
                }
            }

            Direction.West -> {
                for (x in 0..maxX) {
                    if (!movingRocks.first.containsKey(x)) {
                        continue
                    }

                    for (movingY in movingRocks.first[x]!!) {
                        val m = max(
                            staticRocks.second[movingY]?.filter { it < x }?.maxOrNull() ?: -1,
                            result.second[movingY]?.filter { it < x }?.maxOrNull() ?: -1,
                        )
                        addToPairMap(result, m + 1, movingY)
                    }
                }
            }

            Direction.East -> {
                for (x in 0..maxX) {
                    if (!movingRocks.first.containsKey(maxX - x)) {
                        continue
                    }

                    for (movingY in movingRocks.first[maxX - x]!!) {
                        val m = min(
                            staticRocks.second[movingY]?.filter { it > maxX - x }?.minOrNull() ?: (maxX + 1),
                            result.second[movingY]?.filter { it > maxX - x }?.minOrNull() ?: (maxX + 1),
                        )
                        addToPairMap(result, m - 1, movingY)
                    }
                }
            }
        }

        return result

    }

    private fun addToPairMap(
        result: Pair<MutableMap<Int, MutableList<Int>>, MutableMap<Int, MutableList<Int>>>,
        x: Int,
        y: Int
    ) {
        if (result.first.containsKey(x)) {
            result.first[x]!!.add(y)
        } else {
            result.first[x] = mutableListOf(y)
        }
        if (result.second.containsKey(y)) {
            result.second[y]!!.add(x)
        } else {
            result.second[y] = mutableListOf(x)
        }
    }

    private fun createPairMap(pairList: List<Pair<Int, Int>>): Pair<Map<Int, List<Int>>, Map<Int, List<Int>>> =
        pairList.fold(Pair(mutableMapOf<Int, MutableList<Int>>(), mutableMapOf<Int, MutableList<Int>>())) { acc, pair ->
            addToPairMap(acc, pair.first, pair.second)
            acc
        }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)
        val staticRocks = mutableListOf<Pair<Int, Int>>()
        val movingRocks = mutableListOf<Pair<Int, Int>>()

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

        val staticRocksXY = createPairMap(staticRocks)
        var movingRocksXY = createPairMap(movingRocks)

        val original = mutableListOf<Pair<Map<Int, List<Int>>, Map<Int, List<Int>>>>()

        val result = tilt(staticRocksXY, movingRocksXY, lines.lastIndex - 1, lines[0].lastIndex, Direction.North)
        val part1 = result.second.keys.sumOf { (lines.size - it - 1) * result.second[it]!!.size }
        var part2 = 0

        for (i in 0..<1000000000) {

            movingRocksXY = tilt(staticRocksXY, movingRocksXY, lines.lastIndex - 1, lines[0].lastIndex, Direction.North)
            movingRocksXY = tilt(staticRocksXY, movingRocksXY, lines.lastIndex - 1, lines[0].lastIndex, Direction.West)
            movingRocksXY = tilt(staticRocksXY, movingRocksXY, lines.lastIndex - 1, lines[0].lastIndex, Direction.South)
            movingRocksXY = tilt(staticRocksXY, movingRocksXY, lines.lastIndex - 1, lines[0].lastIndex, Direction.East)

            val copy = movingRocksXY.copy()

            val index = original.indexOf(copy)
            if (index != -1) {
                val extra = (1000000000 - (index + 1)) % (i - index)
                part2 =
                    original[index + extra].second.keys.sumOf { (lines.size - it - 1) * original[index + extra].second[it]!!.size }
                break
            }

            original.add(copy)
        }

        return Pair(part1.toString(), part2.toString())
    }

}