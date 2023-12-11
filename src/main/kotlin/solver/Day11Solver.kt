package solver

import Day
import kotlin.math.abs


@Day(2023, 11)
class Day11Solver(val part2Diff: Long = 1000000) : BaseSolver(), ISolver {
    private fun getDistances(gal: Pair<Long, Long>, galaxies: List<Pair<Long, Long>>): List<Long> {
        val distances = mutableListOf<Long>()
        for (galaxy in galaxies) {
            distances.add(
                abs(galaxy.first - gal.first) + abs(galaxy.second - gal.second)
            )
        }

        return distances
    }

    private fun expand(galaxies: List<Pair<Long, Long>>, diff: Long): List<Pair<Long, Long>> {
        val expanded = galaxies.toMutableList()
        val xs = galaxies.map { it.first }
        val minX = xs.min()
        var maxX = xs.max()
        val ys = galaxies.map { it.second }
        val minY = ys.min()
        var maxY = ys.max()

        var x = minX
        while (x < maxX) {
            if (expanded.any { it.first == x }) {
                x++
                continue
            }

            expanded.filter { it.first > x }.forEach {
                expanded.remove(it)
                expanded.add(it.copy(first = it.first + diff))
            }
            maxX += diff
            x += diff + 1
        }

        var y = minY
        while (y < maxY) {
            if (expanded.any { it.second == y }) {
                y++
                continue
            }

            expanded.filter { it.second > y }.forEach {
                expanded.remove(it)
                expanded.add(it.copy(second = it.second + diff))
            }
            maxY += diff
            y += diff + 1
        }


        return expanded
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val galaxies = mutableListOf<Pair<Long, Long>>()
        var minX = Long.MAX_VALUE
        var maxX = Long.MIN_VALUE
        var minY = Long.MAX_VALUE
        var maxY = Long.MIN_VALUE

        for (line in 0..lines.lastIndex) {
            lines[line].forEachIndexed { index, c ->
                val iLong = index.toLong()
                val lLong = line.toLong()
                if (c == '#') {
                    if (iLong < minX) {
                        minX = iLong
                    }
                    if (iLong > maxX) {
                        maxX = iLong
                    }
                    if (line < minY) {
                        minY = lLong
                    }
                    if (lLong > maxY) {
                        maxY = lLong
                    }

                    galaxies.add(Pair(iLong, lLong))
                }
            }
        }
        val part1Galaxies = expand(galaxies, 1)
        val part2Galaxies = expand(galaxies, part2Diff - 1)
        var part1 = 0.toLong()
        for (i in 0..<part1Galaxies.lastIndex) {
            val distances = getDistances(part1Galaxies[i], part1Galaxies.subList(i + 1, part1Galaxies.lastIndex + 1))
            part1 += distances.sum()
        }
        var part2 = 0.toLong()
        for (i in 0..<part2Galaxies.lastIndex) {
            val distances = getDistances(part2Galaxies[i], part2Galaxies.subList(i + 1, part2Galaxies.lastIndex + 1))
            part2 += distances.sum()
        }

        return Pair(part1.toString(), part2.toString())
    }
}