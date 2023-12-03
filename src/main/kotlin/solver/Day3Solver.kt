package solver

import Day

@Day(2023, 3)
class Day3Solver : BaseSolver(), ISolver {

    data class Pos(val x: Int, val y: Int)
    data class NumberPos(val number: Int, val length: Int, val pos: Pos) {
        fun intersects(p: Pos): Boolean {
            return pos.y in p.y - 1..p.y + 1 && p.x - 1 <= pos.x + length && p.x + 1 >= pos.x
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val numbers = mutableListOf<NumberPos>()

        var part1 = 0
        var part2 = 0

        for (i in 0..lines.lastIndex) {
            if (lines[i].isBlank()) {
                continue
            }

            val n = """(\d+)""".toRegex().findAll(lines[i])

            for (num in n) {
                numbers.add(NumberPos(num.value.toInt(), num.range.last - num.range.first, Pos(num.range.first, i)))
            }
        }

        for (i in 0..lines.lastIndex) {
            if (lines[i].isBlank()) {
                continue
            }

            val chars = """[^.\d\s]""".toRegex().findAll(lines[i])

            for (c in chars) {
                var intCount = 0
                var multiplication = 1

                for (n in numbers) {
                    if (n.intersects(Pos(c.range.first, i))) {
                        part1 += n.number
                        if (c.value == "*") {
                            intCount++
                            multiplication *= n.number
                        }
                    }
                }

                if (intCount == 2) {
                    part2 += multiplication
                }
            }
        }


        return Pair(part1.toString(), part2.toString())
    }
}