import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType
import solver.Day1Solver
import solver.Day2Solver
import solver.Day3Solver
import solver.Day4Solver
import solver.Day5Solver
import solver.Day6Solver
import solver.Day7Solver
import solver.Day8Solver
import solver.Day9Solver
import solver.ISolver
import kotlin.system.measureTimeMillis

val aoc_cookie = Key("cookie", stringType)
val config = ConfigurationProperties.fromResource("application.properties")

fun main() {
    val solverMap = mapOf<Int, ISolver>(
        1 to Day1Solver(),
        2 to Day2Solver(),
        3 to Day3Solver(),
        4 to Day4Solver(),
        5 to Day5Solver(),
        6 to Day6Solver(),
        7 to Day7Solver(),
        8 to Day8Solver(),
        9 to Day9Solver()
    )

    val daysAvailable = solverMap.keys.joinToString(", ")

    fun run(day: Int) {
        var solution: Pair<String, String?>?
        val elapsed = measureTimeMillis {
            solution = solverMap[day]?.solve(config[aoc_cookie])
        }

        println("Day $day: Part 1: (${solution?.first}) | Part 2 (${solution?.second}) | Solved in $elapsed ms")
    }

    fun runAll() {
        for (key in solverMap.keys) {
            run(key)
        }
    }

    do {
        println("Select a day to solve, enter 'exit' to quit and 0 to run all tests:")
        println("Days available: $daysAvailable")

        val input = readln()

        when (input) {
            "exit" -> break
            else -> {
                val num = input.toIntOrNull()
                when (num) {
                    null -> println("Invalid Input.")
                    0 -> runAll()
                    else -> run(input.toInt())
                }
            }
        }
    } while (true)
}