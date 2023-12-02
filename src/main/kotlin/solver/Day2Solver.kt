package solver

import Day

@Day(2023, 2)
class Day2Solver : BaseSolver(), ISolver {
    companion object {
        const val MAX_RED = 12
        const val MAX_GREEN = 13
        const val MAX_BLUE = 14
    }

    data class MinCubes(var red: Int, var green: Int, var blue: Int)

    fun getMinCubes(game: String): Pair<Int, MinCubes> {
        val split = game.split(':')
        val gameNumber = split.first().split(' ').last().toInt()

        val min = MinCubes(0, 0, 0)

        for (turn in split.last().split(';')) {
            val cubes = turn.split(',')

            for (cube in cubes) {
                val values = cube.trim().split(' ')

                when (values.last()) {
                    "red" -> if (values.first().toInt() > min.red) {
                        min.red = values.first().toInt()
                    }

                    "green" -> if (values.first().toInt() > min.green) {
                        min.green = values.first().toInt()
                    }

                    "blue" -> if (values.first().toInt() > min.blue) {
                        min.blue = values.first().toInt()
                    }
                }
            }
        }

        return Pair(gameNumber, min)
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val games = getLines(cookie)

        var sumP1 = 0
        var sumP2 = 0

        for (game in games) {
            if (game.isBlank()) {
                continue
            }

            val result = getMinCubes(game)

            sumP2 += result.second.blue * result.second.red * result.second.green

            if (result.second.blue <= MAX_BLUE && result.second.red <= MAX_RED && result.second.green <= MAX_GREEN) {
                sumP1 += result.first
            }
        }

        return Pair(sumP1.toString(), sumP2.toString())
    }
}