package solver

import Day


@Day(2023, 13)
class Day13Solver : BaseSolver(), ISolver {
    private fun checkReflection(
        start: Int,
        end: Int,
        pattern: List<String>
    ): Int {
        var diffCount = 0
        val lIdx = start + (end - start) / 2

        for (i in start..lIdx) {
            val diff = countDifferences(pattern[i], pattern[end - (i - start)])
            diffCount += diff
            if (diffCount > 1) {
                return -1
            }
        }

        return diffCount

    }

    private fun countDifferences(a: String, b: String): Int {
        var count = 0

        for (i in 0..a.lastIndex) {
            if (a[i] != b[i]) {
                count++
            }
        }

        return count
    }

    private fun findMirror(pattern: List<String>): Pair<Int, Int> {
        val start = pattern.first()
        val end = pattern.last()
        val lines = pattern.foldIndexed(
            Pair(
                mutableListOf<Int>(),
                mutableListOf<Int>()
            )
        ) { index, acc, s ->
            if (index % 2 != 0) {
                val diffS = countDifferences(start, s)
                if (diffS < 2) {
                    acc.first.add(index)
                }
            }
            if ((pattern.lastIndex - index) % 2 != 0) {
                val diffE = countDifferences(end, s)
                if (diffE < 2) {
                    acc.second.add(index)
                }
            }

            acc
        }

        var part1 = 0
        var part2 = 0

        if (lines.first.isNotEmpty()) {
            for (index in lines.first) {
                val diff = checkReflection(0, index, pattern)
                if (diff == 1) {
                    part2 = index / 2 + 1
                } else if (diff == 0) {
                    part1 = index / 2 + 1
                }
            }
        }

        if (lines.second.isNotEmpty()) {
            for (index in lines.second) {
                val diff = checkReflection(index, pattern.lastIndex, pattern)
                if (diff == 1) {
                    part2 = index + (pattern.lastIndex - index) / 2 + 1
                } else if (diff == 0) {
                    part1 = index + (pattern.lastIndex - index) / 2 + 1
                }
            }
        }

        return Pair(part1, part2)
    }

    private fun rotatePattern(pattern: List<String>): List<String> {
        val res = mutableListOf<String>()

        for (i in 0..pattern[0].lastIndex) {
            var r = ""
            for (line in pattern) {
                r += line[i]
            }
            res.add(r)
        }

        return res
    }

    private fun checkPattern(pattern: List<String>): Pair<Int, Int> {
        var part1 = 0
        var part2 = 0
        val horizontal = findMirror(pattern)
        if (horizontal.first > 0) {
            part1 = horizontal.first * 100
        }
        if (horizontal.second > 0) {
            part2 = horizontal.second * 100
        }

        val vertical = findMirror(rotatePattern(pattern))
        if (vertical.first > 0) {
            part1 = vertical.first
        }
        if (vertical.second > 0) {
            part2 = vertical.second
        }
        return Pair(part1, part2)
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)
        val emptyLines = lines.foldIndexed(mutableListOf(0)) { index, acc, s ->
            if (s.isEmpty()) {
                acc.add(index + 1)
            }
            acc
        }

        var part1: Long = 0.toLong()
        var part2: Long = 0.toLong()
        for (index in 0..<emptyLines.lastIndex) {
            val pattern = lines.subList(emptyLines[index], emptyLines[index + 1] - 1)
            val res = checkPattern(pattern)
            part1 += res.first
            part2 += res.second
        }

        return Pair(part1.toString(), part2.toString())
    }
}