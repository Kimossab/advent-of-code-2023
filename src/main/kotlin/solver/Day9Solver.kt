package solver

import Day


@Day(2023, 9)
class Day9Solver : BaseSolver(), ISolver {
    class Sequence(private val seq: List<Int>) {
        val subSequence: Sequence?

        init {
            subSequence = if (seq.all { it == 0 }) {
                null
            } else {
                val list = mutableListOf<Int>()

                for (i in 0..<seq.lastIndex) {
                    list.add(seq[i + 1] - seq[i])
                }

                Sequence(list)
            }
        }

        fun getNext(): Int {
            if (subSequence == null) {
                return 0
            }
            return seq.last() + subSequence.getNext()
        }

        fun getPrevious(): Int {
            if (subSequence == null) {
                return 0
            }
            val prev = seq.first() - subSequence.getPrevious()
            return prev
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        var part1 = 0
        var part2 = 0

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val seq = Sequence(line.split(" ").map { it.toInt() })
            part1 += seq.getNext()
            part2 += seq.getPrevious()
        }


        return Pair(part1.toString(), part2.toString())
    }
}