package solver

import Day
import kotlin.math.pow

@Day(2023, 4)
class Day4Solver : BaseSolver(), ISolver {
    data class Card(val number: Int, val winning: Set<Int>, val yourNumbers: Set<Int>) {
        val intersection = yourNumbers.intersect(winning)
        fun points(): Int {
            if (intersection.isEmpty()) {
                return 0
            }

            return 2.0.pow(intersection.size - 1.0).toInt()
        }

        fun copiesGiven(): List<Int> {
            val list = mutableListOf<Int>()
            for (i in 1..intersection.size) {
                list.add(number + i)
            }
            return list
        }

        fun copyCount(list: List<Card>, countMap: MutableMap<Int, Int>): Int {
            if (countMap.containsKey(number)) {
                return countMap[number]!!
            }
            val cardList = copiesGiven()
            var count = 1
            for (c in cardList) {
                count += if (countMap.containsKey(c)) {
                    countMap[c]!!
                } else {
                    list.find { it.number == c }?.copyCount(list, countMap) ?: 0
                }
            }
            countMap[number] = count
            return countMap[number]!!
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        var part1 = 0

        val cardCount = mutableMapOf<Int, Int>()

        val cardList = mutableListOf<Card>()

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val lSplit = line.split(":")
            val cardNumber = lSplit.first().split(" ").last().toInt()
            val numbers = lSplit.last().split("|")
            val winning = numbers.first().split(" ").mapNotNull { it.toIntOrNull() }.toSet()
            val yourNumbers = numbers.last().split(" ").mapNotNull { it.toIntOrNull() }.toSet()
            val card = Card(cardNumber, winning, yourNumbers)

            part1 += card.points()

            cardList.add(card)
        }

        val part2 = cardList.sumOf { it.copyCount(cardList, cardCount) }

        return Pair(part1.toString(), part2.toString())
    }
}