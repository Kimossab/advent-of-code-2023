package solver

import Day


@Day(2023, 7)
class Day7Solver : BaseSolver(), ISolver {
    companion object {
        interface ICard {
            val character: Char
            val strength: Int
        }

        enum class Card(override val character: Char, override val strength: Int) : ICard {
            Ace('A', 13),
            King('K', 12),
            Queen('Q', 11),
            Jack('J', 10),
            Trump('T', 9),
            Nine('9', 8),
            Eight('8', 7),
            Seven('7', 6),
            Six('6', 5),
            Five('5', 4),
            Four('4', 3),
            Three('3', 2),
            Two('2', 1)
        }

        enum class CardPart2(override val character: Char, override val strength: Int) : ICard {
            Ace('A', 13),
            King('K', 12),
            Queen('Q', 11),
            Joker('J', 0),
            Trump('T', 9),
            Nine('9', 8),
            Eight('8', 7),
            Seven('7', 6),
            Six('6', 5),
            Five('5', 4),
            Four('4', 3),
            Three('3', 2),
            Two('2', 1)
        }

        enum class HandType(val strength: Int) {
            FiveOfAKind(6),
            FourOfAKind(5),
            FullHouse(4),
            ThreeOfAKind(3),
            TwoPair(2),
            OnePair(1),
            HighCard(0)
        }

        fun getHandType(cards: List<ICard>): HandType {
            var highestRank: HandType = HandType.HighCard
            val withoutJoker = cards.filter { it != CardPart2.Joker }

            val cardAlternatives =
                if (withoutJoker.size == 5 || withoutJoker.isEmpty()) listOf(cards) else withoutJoker.map { card ->
                    cards.map { if (it == CardPart2.Joker) card else it }
                }

            for (cardList in cardAlternatives) {
                val visited = mutableMapOf<ICard, Int>()
                for (i in 0..cardList.lastIndex) {
                    if (visited.containsKey(cardList[i])) {
                        continue
                    }

                    var equalCount = 1

                    for (n in 0..cardList.lastIndex) {
                        if (n == i) {
                            continue
                        }
                        if (cardList[n] == cardList[i]) {
                            equalCount++
                        }
                    }

                    val rank = getHandRank(equalCount, visited)

                    if (highestRank.strength < rank.strength) {
                        highestRank = rank
                    }

                    visited[cardList[i]] = equalCount
                }
            }
            return highestRank
        }

        private fun getHandRank(
            equalCount: Int,
            visited: MutableMap<ICard, Int>
        ): HandType {
            val rank = when (equalCount) {
                5 -> HandType.FiveOfAKind
                4 -> HandType.FourOfAKind
                3 -> when (visited.size) {
                    2 -> HandType.ThreeOfAKind
                    1 -> if (visited.values.first() == 2) {
                        HandType.FullHouse
                    } else {
                        HandType.ThreeOfAKind
                    }

                    else -> HandType.ThreeOfAKind
                }

                2 -> when (visited.size) {
                    3 -> HandType.OnePair
                    2 -> {
                        if (visited.values.any { it == 2 }) {
                            HandType.TwoPair
                        } else {
                            HandType.OnePair
                        }
                    }

                    1 -> {
                        if (visited.values.first() == 3) {
                            HandType.FullHouse
                        } else if (visited.values.first() == 2) {
                            HandType.TwoPair
                        } else {
                            HandType.OnePair
                        }
                    }

                    else -> HandType.OnePair
                }

                else -> HandType.HighCard
            }
            return rank
        }
    }

    class Hand(private val cards: List<ICard>, val bid: Int) : Comparable<Hand> {
        private val type: HandType = getHandType(cards)

        override fun compareTo(other: Hand): Int {
            if (type.strength > other.type.strength) {
                return 1
            }
            if (type.strength < other.type.strength) {
                return -1
            }

            for (i in 0..cards.lastIndex) {
                if (cards[i].strength > other.cards[i].strength) {
                    return 1
                }
                if (cards[i].strength < other.cards[i].strength) {
                    return -1
                }
            }

            return 0
        }
    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val hands = mutableListOf<Hand>()
        val handsPart2 = mutableListOf<Hand>()

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val split = line.split(" ")
            val cards = split[0].map { char -> Card.entries.first { it.character == char } }
            val cardsPart2 = split[0].map { char -> CardPart2.entries.first { it.character == char } }
            hands.add(Hand(cards, split[1].toInt()))
            handsPart2.add(Hand(cardsPart2, split[1].toInt()))
        }

        val part1 = hands.sorted().foldIndexed(0) { index, acc, hand -> acc + hand.bid * (index + 1) }.toString()
        val part2 = handsPart2.sorted().foldIndexed(0) { index, acc, hand ->
            acc + hand.bid * (index + 1)
        }.toString()

        return Pair(part1, part2)
    }
}