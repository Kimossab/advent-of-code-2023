package solver

import Day


@Day(2023, 12)
class Day12Solver : BaseSolver(), ISolver {
    data class Test(var lastBroken: Boolean, var currentSequence: MutableList<Long>, var count: Long)

    fun checkObjectiveStartsWith(objective: List<Long>, t: Test, forceValue: Boolean = false): Boolean {
        if (forceValue) {
            if (objective.size != t.currentSequence.size) {
                return false
            }

            for (i in 0..objective.lastIndex) {
                if (t.currentSequence[i] != objective[i]) {
                    return false
                }
            }

            return true
        }

        if (t.currentSequence.isEmpty()) {
            return true
        }

        if (t.currentSequence.size > objective.size) {
            return false
        }

        for (i in 0..<t.currentSequence.lastIndex) {
            if (t.currentSequence[i] != objective[i]) {
                return false
            }
        }

        if (t.lastBroken) {
            if (t.currentSequence.last() > objective[t.currentSequence.lastIndex]) {
                return false
            }
        } else {
            if (t.currentSequence.last() != objective[t.currentSequence.lastIndex]) {
                return false
            }
        }

        return true
    }

    fun removeDuplicates(testList: List<Test>): List<Test> {
        val m = mutableMapOf<MutableList<Long>, Long>()

        for (t in testList) {
            if (m.containsKey(t.currentSequence)) {
                continue
            }
            m[t.currentSequence] = testList.fold(t.count) { acc, it ->
                if (t !== it && it.currentSequence == t.currentSequence) {
                    acc + it.count
                } else {
                    acc
                }
            }
        }

        return m.keys.map {
            Test(false, it, m[it]!!)
        }
    }

    fun getAlternatives(map: String, objective: List<Long>): Long {
        var results = mutableListOf(Test(false, mutableListOf(), 1))

        for (c in map) {
            when (c) {
                '.' -> results = removeDuplicates(results).toMutableList()
                '#' -> results.forEach {
                    if (!it.lastBroken) {
                        it.currentSequence.add(1)
                    } else {
                        it.currentSequence[it.currentSequence.lastIndex]++
                    }
                    it.lastBroken = true
                }

                '?' -> {
                    val range = 0..results.lastIndex
                    for (i in range) {
                        results.add(results[i].copy(currentSequence = results[i].currentSequence.toMutableList()))
                        if (!results[i + range.last + 1].lastBroken) {
                            results[i + range.last + 1].currentSequence.add(1)
                        } else {
                            results[i + range.last + 1].currentSequence[results[i + range.last + 1].currentSequence.lastIndex]++
                        }
                        results[i + range.last + 1].lastBroken = true
                        results[i].lastBroken = false
                    }

                    val dots = results.filter { !it.lastBroken }
                    results = results.filter { it.lastBroken }.toMutableList()
                    results.addAll(removeDuplicates(dots))
                }
            }

            results = results.filter {
                checkObjectiveStartsWith(objective, it)
            }.toMutableList()
        }

        return results.fold(0.toLong()) { acc, it ->
            if (checkObjectiveStartsWith(objective, it, true)) {
                acc + it.count
            } else {
                acc
            }
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie).filter { it.isNotBlank() }.map { it.split(" ") }

        var part1 = 0.toLong()
        var part2 = 0.toLong()
        for (pair in lines) {
            val obj = pair.last().split(",").map { it.toLong() }
            val start = pair.first()
            part1 += getAlternatives(start, obj)
            val obj2 = obj.toMutableList()
            obj2.addAll(obj)
            obj2.addAll(obj)
            obj2.addAll(obj)
            obj2.addAll(obj)
            part2 += getAlternatives("$start?$start?$start?$start?$start", obj2).toLong()
        }

        return Pair(part1.toString(), part2.toString())
    }
}