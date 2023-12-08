package solver

import Day
import kotlin.math.ceil


@Day(2023, 8)
class Day8Solver : BaseSolver(), ISolver {
    companion object {
        val stepsMap = mutableMapOf<String, Pair<List<Int>, String>>()
    }

    private fun doSteps(
        nodeMap: Map<String, Pair<String, String>>,
        node: String,
        instructions: CharArray
    ): Int {
        var mutableNode = node
        var index = 0
        var count = 0

        while (true) {
            if (index > instructions.lastIndex) {
                index = 0
            }

            mutableNode = when (instructions[index]) {
                'L' -> nodeMap[mutableNode]!!.first
                'R' -> nodeMap[mutableNode]!!.second
                else -> mutableNode
            }
            count++
            index++

            if (mutableNode == "ZZZ") {
                return count
            }
        }
    }

    private fun getAllSteps(
        nodeMap: Map<String, Pair<String, String>>,
        node: String,
        instructions: CharArray
    ): Pair<List<Int>, String> {
        if (stepsMap.containsKey(node)) {
            return stepsMap[node]!!
        }

        var mutableNode = node
        val indexes = mutableListOf<Int>()

        for (instruction in 0..instructions.lastIndex) {
            mutableNode = when (instructions[instruction]) {
                'L' -> nodeMap[mutableNode]!!.first
                'R' -> nodeMap[mutableNode]!!.second
                else -> mutableNode
            }

            if (mutableNode.endsWith("Z")) {
                indexes.add(instruction)
            }
        }

        stepsMap[node] = Pair(indexes, mutableNode)
        return stepsMap[node]!!
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val nodeMap = mutableMapOf<String, Pair<String, String>>()

        val lines = getLines(cookie)

        val instructions = lines.first().toCharArray()

        for (i in 1..lines.lastIndex) {
            if (lines[i].isBlank()) {
                continue
            }

            val matches = """(?<name>\S*)\s=\s\((?<left>[^,]*),\s(?<right>.*)\)""".toRegex().find(lines[i])?.groups
                ?: throw Exception("MEH")

            nodeMap[matches["name"]?.value ?: ""] = Pair(matches["left"]?.value ?: "", matches["right"]?.value ?: "")
        }

        for (node in nodeMap.keys) {
            getAllSteps(nodeMap, node, instructions)
        }

        val nodesThatEndInA = nodeMap.keys.filter { it[it.lastIndex] == 'A' }.toMutableList()
        // Basically any node that when the loop starts on it, it will have a Z somewhere in that loop
        val nodesThatContainZInALoop = stepsMap.keys.filter { stepsMap[it]!!.first.isNotEmpty() }

        val mapTest = getSequenceFormula(nodesThatEndInA, nodesThatContainZInALoop)

        // first * x + (second * x - 1) = y
        // (first + second) x - 1 = y
        val equations = mapTest.map { it.first + (it.second - it.first * 2) }

        var jump = equations.minOfOrNull { it - 1 } ?: 0
        var x = jump
        var iteration = 1

        while (true) {
            /*
            a*46+b = 1 // first time there's a Z
            a*93+b = 2 // second time there's a Z

            46a = 1-b
            93a = 2-b

            a = (1-b)/46
            93(1-b)/46 = 2-b

            a = (1-b)/46
            93(1-b) = 46(2-b)

            b=1/47
            a = 1/47

            ax+b=y
            x/47+1/47 = y

            47 = 46*2
             */
            val values =
                equations.map { value -> x / value.toDouble() + 1 / value.toDouble() }

            // how many of them are integers
            // if they're integers then it's an iteration where the node will have a Z in the loop
            val c = values.count { it.toLong() == ceil(it).toLong() }

            if (c == values.size) {
                break
            }

            if (c > iteration) {
                jump = x
                iteration = c
            }

            x += (jump + 1)
        }

        // In my case they all have a Z at step 268, so if it has a Z in a loop it's ever only 1, and it's always at that step
        // we need to add 1 because the x is always at the end of the loop, so it's the next loop + the number of steps
        val part2 = x * instructions.size + stepsMap.values.first { it.first.isNotEmpty() }.first.first() + 1

        return Pair(doSteps(nodeMap, "AAA", instructions).toString(), part2.toString())
    }

    private fun getSequenceFormula(
        nodesThatEndInA: MutableList<String>,
        nodesThatContainZInALoop: List<String>
    ): List<Pair<Long, Long>> {
        var loop2: Long = 0
        var nodes = nodesThatEndInA.toList()
        val zeroL = 0.toLong()
        val formulas = nodes.map { Pair(zeroL, zeroL) }.toMutableList()

        while (formulas.any { it.first == zeroL || it.second == zeroL }) {
            val t = nodes.map { stepsMap[it]!!.second }

            for (n in 0..nodes.lastIndex) {
                if (nodesThatContainZInALoop.contains(nodes[n])) {
                    if (formulas[n].first == zeroL) {
                        formulas[n] = Pair(loop2, zeroL)
                    } else if (formulas[n].second == zeroL) {
                        formulas[n] = Pair(formulas[n].first, loop2)
                    }
                }
            }

            nodes = t
            loop2++
        }
        return formulas
    }
}