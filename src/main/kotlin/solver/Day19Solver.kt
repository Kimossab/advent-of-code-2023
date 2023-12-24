package solver

import Day


@Day(2023, 19)
class Day19Solver : BaseSolver(), ISolver {
    enum class PartCategory(val char: Char) {
        ExtremelyCoolLooking('x'),
        Musical('m'),
        Aerodynamic('a'),
        Shiny('s')
    }

    enum class Operation(val char: Char) {
        GreaterThan('>'),
        LessThan('<')
    }

    enum class WorkflowResult(val string: String) {
        Accept("A"),
        Reject("R")
    }

    data class Part(val categoryValues: Map<PartCategory, Int>) {
        companion object {
            fun parse(string: String): Part {
                val map = string.substring(1, string.lastIndex).split(",")
                    .fold(mutableMapOf<PartCategory, Int>()) { acc, s ->
                        val t = s.split("=")
                        val category = PartCategory.entries.first { it.char == t.first()[0] }
                        val value = t.last().toInt()

                        acc[category] = value
                        acc
                    }

                return Part(map)
            }
        }
    }

    data class Rule(
        val category: PartCategory?,
        val operation: Operation?,
        val value: Int?,
        val destination: String?,
        val result: WorkflowResult?
    ) {
        companion object {
            fun parse(string: String): Rule {
                val compareResult = string.split(":")
                val last = compareResult.last()
                val destination = if (last == "A" || last == "R") null else last
                val result =
                    if (last == "A") WorkflowResult.Accept else if (last == "R") WorkflowResult.Reject else null

                return if (compareResult.size == 1) {
                    Rule(null, null, null, destination, result)
                } else {
                    val greaterSplit = compareResult.first().split(">")
                    if (greaterSplit.size == 1) {
                        val lessSplit = compareResult.first().split("<")
                        val category = PartCategory.entries.first { lessSplit.first()[0] == it.char }
                        val value = lessSplit.last().toInt()

                        Rule(category, Operation.LessThan, value, destination, result)
                    } else {
                        val category = PartCategory.entries.first { greaterSplit.first()[0] == it.char }
                        val value = greaterSplit.last().toInt()

                        Rule(category, Operation.GreaterThan, value, destination, result)
                    }
                }
            }
        }

        fun test(part: Part): String? {
            if (category == null) {
                return destination ?: result?.string
            }

            val v = part.categoryValues[category]!!

            return when (operation) {
                Operation.GreaterThan -> if (v > value!!) {
                    destination ?: result?.string
                } else {
                    null
                }

                Operation.LessThan -> if (v < value!!) {
                    destination ?: result?.string
                } else {
                    null
                }

                null -> destination ?: result?.toString()
            }
        }

        fun split(data: Map<PartCategory, Pair<Int, Int>>): Pair<Map<PartCategory, Pair<Int, Int>>, Map<PartCategory, Pair<Int, Int>>?> {
            if (operation != null) {
                val positive = data.toMutableMap()
                val negative = data.toMutableMap()

                val listPos = if (operation == Operation.LessThan) Pair(
                    data[category]!!.first,
                    value!! - 1
                ) else Pair(value!! + 1, data[category]!!.second)
                val listNeg = if (operation == Operation.LessThan) Pair(
                    value,
                    data[category]!!.second
                ) else Pair(data[category]!!.first, value)

                positive[category!!] = listPos
                negative[category] = listNeg

                return Pair(positive, negative)
            } else {
                return Pair(data, null)
            }
        }
    }

    data class Workflow(val name: String, val ruleSet: List<Rule>) {
        fun test(part: Part, workflowList: List<Workflow>): WorkflowResult {
            for (rule in ruleSet) {
                val result = rule.test(part) ?: continue

                if (result == WorkflowResult.Accept.string) {
                    return WorkflowResult.Accept
                }

                if (result == WorkflowResult.Reject.string) {
                    return WorkflowResult.Reject
                }

                return workflowList.find { it.name == result }!!.test(part, workflowList)
            }

            return WorkflowResult.Reject
        }

    }

    fun recursiveGetAccepted(
        data: Map<PartCategory, Pair<Int, Int>>,
        workflow: Workflow,
        workflowList: List<Workflow>
    ): List<Map<PartCategory, Pair<Int, Int>>> {
        val result = mutableListOf<Map<PartCategory, Pair<Int, Int>>>()
        var combo = data
        for (rule in workflow.ruleSet) {
            val split = rule.split(combo)

            if (rule.result == WorkflowResult.Accept) {
                result.add(split.first)
            } else if (rule.result != WorkflowResult.Reject) {
                val nwFlow = workflowList.first { it.name == rule.destination }
                val r = recursiveGetAccepted(split.first, nwFlow, workflowList)

                result.addAll(r)
            }

            combo = split.second ?: mapOf()
        }

        return result
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val workflowList = mutableListOf<Workflow>()
        val parts = mutableListOf<Part>()

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }

            val workflowMatch = """^(?<name>[^{]+)\{(?<rules>[^}]*)\}""".toRegex().find(line)

            if (workflowMatch != null) {
                val rules = workflowMatch.groups["rules"]!!.value.split(",").map { Rule.parse(it) }

                workflowList.add(
                    Workflow(
                        workflowMatch.groups["name"]!!.value,
                        rules
                    )
                )
            } else {
                parts.add(Part.parse(line))
            }
        }

        val inWorkflow = workflowList.first { it.name == "in" }

        val accepted =
            parts.filter { inWorkflow.test(it, workflowList) == WorkflowResult.Accept }.sumOf { part ->
                part.categoryValues.values.sum()
            }

        val comboData = mapOf(
            PartCategory.Aerodynamic to (1 to 4000),
            PartCategory.ExtremelyCoolLooking to (1 to 4000),
            PartCategory.Musical to (1 to 4000),
            PartCategory.Shiny to (1 to 4000)
        )

        val acceptedList = recursiveGetAccepted(comboData, inWorkflow, workflowList)

        val part2 = acceptedList.sumOf {
            it.values.fold(1.toLong()) { acc, pair ->
                acc * (pair.second - pair.first + 1)
            }
        }

        return Pair(accepted.toString(), part2.toString())
    }
}