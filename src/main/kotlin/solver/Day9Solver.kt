package solver

import Day


@Day(2023, 9)
class Day9Solver : BaseSolver(), ISolver {
    private fun getNextPrev(sequence: List<Int>): Pair<Int, Int> {
        if (sequence.all { it == 0 }) {
            return Pair(0, 0)
        }

        val list = mutableListOf<Int>()

        for (i in 0..<sequence.lastIndex) {
            list.add(sequence[i + 1] - sequence[i])
        }

        val subSeqNextPrev = getNextPrev(list)
        return Pair(sequence.first() - subSeqNextPrev.first, sequence.last() + subSeqNextPrev.second)
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        var part1 = 0
        var part2 = 0

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }

            val res = getNextPrev(line.split(" ").map { it.toInt() })
            part1 += res.second
            part2 += res.first
        }
        
        return Pair(part1.toString(), part2.toString())
    }
}