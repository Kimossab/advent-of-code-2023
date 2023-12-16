package solver

import Day
import kotlin.math.max
import kotlin.math.min


@Day(2023, 15)
class Day15Solver : BaseSolver(), ISolver {
    private fun hash(input: String) = input.fold(0) { acc, it ->
        (acc + it.hashCode()) * 17 % 256
    }


    override fun solve(cookie: String?): Pair<String, String?> {
        val input = getInput(cookie).trim()

        val boxes = List(256) { mutableListOf<Pair<String, Int>>() }

        val part1 = input.split(",").sumOf { hash(it) }

        for (i in input.split(",")) {
            val match = """(?<label>[^=-]*)(?<op>[=-])(?<power>\d)?""".toRegex().find(i)
            val label = match?.groups?.get("label")?.value
            val op = match?.groups?.get("op")?.value
            val power = match?.groups?.get("power")?.value

            if(label == null || op == null) {
                throw Error("MEH")
            }

            val box = hash(label)

            when(op) {
                "=" -> {
                    val idx = boxes[box].indexOfFirst { it.first == label }
                    val p = Pair(label, power?.toInt() ?: 0)
                    if(idx == -1) {
                        boxes[box].add(p)
                    } else {
                        boxes[box][idx] = p
                    }
                }
                "-" -> boxes[box].removeIf { f -> f.first == label }
            }
        }

        val part2 = boxes.foldIndexed(0.toLong()) {
            index, acc, pairs ->
            acc + pairs.foldIndexed(0.toLong()) {
                indexPair, accPair, pair -> accPair + (index +1) * (indexPair + 1) * pair.second
            }
        }

        return Pair(part1.toString(), part2.toString())
    }
}