package solver

import Day

@Day(2023, 1)
class Day1Solver : BaseSolver(), ISolver {
    companion object {
        val numbersPart1 = mapOf(
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9
        )
        val numbersPart2 = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        val numbersPart2Regex = numbersPart1 + numbersPart2
        val part1RegexSingle = """\d""".toRegex()
        val part1Regex = """(?<first>\d).*(?<last>\d)""".toRegex()
        val part2Regex =
            """(?<first>one|two|three|four|five|six|seven|eight|nine|\d).*(?<last>one|two|three|four|five|six|seven|eight|nine|\d)""".toRegex()
        val part2RegexSingle =
            """(one|two|three|four|five|six|seven|eight|nine|\d)""".toRegex()
    }

    private fun getValueRegex(line: String): Pair<Int, Int> {
        val p1 = part1Regex.find(line)?.groups
        val p1V = if (p1 == null) {
            val v = part1RegexSingle.find(line)?.value?.toIntOrNull() ?: 0
            v * 10 + v
        } else {
            val p1L = p1["last"]?.value?.toIntOrNull() ?: 0
            val p1F = p1["first"]?.value?.toIntOrNull() ?: p1L

            p1F * 10 + p1L
        }

        val p2 = part2Regex.find(line)?.groups
        val p2V = if (p2 == null) {
            val v = part2RegexSingle.find(line)?.value?.toIntOrNull() ?: 0
            v * 10 + v
        } else {
            val p2L = numbersPart2Regex[p2["last"]?.value] ?: 0
            val p2F = numbersPart2Regex[p2["first"]?.value] ?: p2L

            p2F * 10 + p2L
        }

        return Pair(p1V, p2V)
    }

    fun getValue(line: String): Pair<Int, Int> {
        var p1First: Int? = null
        var p2First: Int? = null

        var p1Last: Int? = null
        var p2Last: Int? = null

        var p1LastCanBeOverriden = true
        var p2LastCanBeOverriden = true

        for (i in 0..line.length) {
            val substrStart = line.substring(i, line.length)
            val substrEnd = line.substring(0, line.length - i)

            for (num in numbersPart1) {
                if (substrStart.startsWith(num.key)) {
                    if (p1First == null) {
                        p1First = num.value
                    }
                    if (p2First == null) {
                        p2First = num.value
                    }
                    if (p1Last == null) {
                        p1Last = num.value
                    }
                    if (p2Last == null) {
                        p2Last = num.value
                    }
                }

                if (substrEnd.endsWith(num.key)) {
                    if (p1Last == null || p1LastCanBeOverriden) {
                        p1Last = num.value
                        p1LastCanBeOverriden = false
                    }
                    if (p2Last == null || p2LastCanBeOverriden) {
                        p2Last = num.value
                        p2LastCanBeOverriden = false
                    }
                }
            }

            for (num in numbersPart2) {
                if (substrStart.startsWith(num.key)) {
                    if (p2First == null) {
                        p2First = num.value
                    }
                }

                if (substrEnd.endsWith(num.key)) {
                    if (p2Last == null || p2LastCanBeOverriden) {
                        p2Last = num.value
                        p2LastCanBeOverriden = false
                    }
                }
            }

            if (p1First != null && !p1LastCanBeOverriden && p2First != null && !p2LastCanBeOverriden) {
                return Pair(p1First * 10 + (p1Last ?: 0), p2First * 10 + (p2Last ?: 0))
            }
        }

        return Pair((p1First ?: 0) * 10 + (p1Last ?: 0), (p2First ?: 0) * 10 + (p2Last ?: 0))
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        var sum1 = 0
        var sum2 = 0

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val value = getValueRegex(line)

            sum1 += value.first
            sum2 += value.second
        }

        return Pair(sum1.toString(), sum2.toString())
    }
}