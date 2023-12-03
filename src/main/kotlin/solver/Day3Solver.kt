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
        val characters = mutableListOf<Pos>()
        val gears = mutableListOf<Pos>()

        for (i in 0..lines.lastIndex) {
            if (lines[i].isBlank()) {
                continue
            }

            val n = """(\d+)""".toRegex().findAll(lines[i])
            val allGears = """\*""".toRegex().findAll(lines[i])
            val chars = """[^.*\d\s]""".toRegex().findAll(lines[i])

            for (num in n) {
                numbers.add(NumberPos(num.value.toInt(), num.range.last - num.range.first, Pos(num.range.first, i)))
            }
            for (g in allGears) {
                val p = Pos(g.range.first, i)
                gears.add(p)
                characters.add(p)
            }
            for (c in chars) {
                characters.add(Pos(c.range.first, i))
            }
        }

        var part1 = 0
        var part2 = 0

        for (n in numbers) {
            var intersects = false

            for (c in characters) {
                if (n.intersects(c)) {
                    intersects = true
                    break
                }
            }

            if (intersects) {
                part1 += n.number
            }
        }

        for (g in gears) {
            var mul = 1
            var num = 0
            for (n in numbers) {
                if (n.intersects(g)) {
                    num++
                    mul *= n.number
                }
            }

            if (num == 2) {
                part2 += mul
            }
        }


        return Pair(part1.toString(), part2.toString())
    }
}