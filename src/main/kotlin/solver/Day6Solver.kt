package solver

import Day
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

@Day(2023, 6)
class Day6Solver : BaseSolver(), ISolver {
    fun quadratic(a: Double, b: Double, c: Double): Pair<Double, Double> {
        val s = sqrt(b.pow(2) - 4 * a * c)
        val xp = (-b + s) / (2 * a)
        val xn = (-b - s) / (2 * a)

        return Pair(xp, xn)
    }

    fun solveForTimeDistance(time: Long, distance: Long): Long {
        val t = time.toDouble()
        val d = -distance.toDouble()

        val solution = quadratic(-1.0, t, d)
        val xp = solution.first
        val xn = solution.second
        val roundedXp = (if (ceil(xp) == xp) xp + 1 else ceil(xp)).toLong()
        val roundedXn = (if (floor(xn) == xn) xn - 1 else floor(xn)).toLong()
        return roundedXn - roundedXp + 1
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val times = lines[0].split(" ").mapNotNull { it.toLongOrNull() }
        val distances = lines[1].split(" ").mapNotNull { it.toLongOrNull() }

        var part1: Long = 1

        for (i in 0..times.lastIndex) {
            /*
            x -> time pressing button
            v -> speed
            d -> distance record

            v * (time - x) = d
            v = x

            x * (time - x) = d
            -x^2 + time * x = d
            -x^2 + time * x - d = 0
            -x^2 + time * x - d = 0

            val t = times[i].toDouble()
            val d = distances[i].toDouble()
            val s = sqrt(t.pow(2) - 4 * -1 * d)
            val xp = (-t + s)/(2*-1)
            val xn = (-t - s)/(2*-1)
             */


            part1 *= solveForTimeDistance(times[i], distances[i])
        }

        val part2Time = lines[0].split(":").last().replace(" ", "").toLong()
        val part2Distance = lines[1].split(":").last().replace(" ", "").toLong()

        return Pair(part1.toString(), solveForTimeDistance(part2Time, part2Distance).toString())
    }
}