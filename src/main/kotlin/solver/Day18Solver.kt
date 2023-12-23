package solver

import Day


@Day(2023, 18)
class Day18Solver : BaseSolver(), ISolver {
    data class Pos(val x: Int, val y: Int)
    data class Pos2(val x: Long, val y: Long)

    enum class Direction(val character: Char, val num: Int) {
        Up('U', 3),
        Right('R', 0),
        Down('D', 1),
        Left('L', 2)
    }

    fun getNextPos(pos: Pos, direction: Direction, distance: Int): Pos {
        return when (direction) {
            Direction.Up -> Pos(pos.x, pos.y + distance)
            Direction.Right -> Pos(pos.x + distance, pos.y)
            Direction.Down -> Pos(pos.x, pos.y - distance)
            Direction.Left -> Pos(pos.x - distance, pos.y)
        }
    }

    fun getNextPos(pos: Pos2, direction: Direction, distance: Long): Pos2 {
        return when (direction) {
            Direction.Up -> Pos2(pos.x, pos.y + distance)
            Direction.Right -> Pos2(pos.x + distance, pos.y)
            Direction.Down -> Pos2(pos.x, pos.y - distance)
            Direction.Left -> Pos2(pos.x - distance, pos.y)
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        var area1 = 0
        var perimeter1 = 0
        var previous1 = Pos(0, 0)
        var area2 = 0.toLong()
        var perimeter2 = 0.toLong()
        var previous2 = Pos2(0, 0)

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }

            val matches =
                """(?<direction>[UDLR])\s(?<distance>\d*)\s\(#(?<distance2>[\da-f]{5})(?<direction2>[\da-f])\)""".toRegex()
                    .find(line)

            val groups = matches!!.groups

            val direction1 = Direction.entries.find { it.character == groups["direction"]!!.value[0] }!!
            val direction2 = Direction.entries.find { it.num == groups["direction2"]!!.value.toInt() }!!
            val distance1 = groups["distance"]!!.value.toInt()
            val distance2 = groups["distance2"]!!.value.toLong(16)

            val nPos1 = getNextPos(previous1, direction1, distance1)
            val nPos2 = getNextPos(previous2, direction2, distance2)

            area1 += previous1.x * nPos1.y - previous1.y * nPos1.x
            area2 += previous2.x * nPos2.y - previous2.y * nPos2.x

            perimeter1 += distance1
            perimeter2 += distance2

            previous1 = nPos1
            previous2 = nPos2
        }

        val areaTotal1 = if (area1 < 0) area1 / -2 + perimeter1 / 2 + 1 else area1 / 2 + perimeter1 / 2 + 1
        val areaTotal2 = if (area2 < 0) area2 / -2 + perimeter2 / 2 + 1 else area2 / 2 + perimeter2 / 2 + 1

        return Pair(areaTotal1.toString(), areaTotal2.toString())
    }
}