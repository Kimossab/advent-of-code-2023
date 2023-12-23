package solver

import Day
import java.util.*


@Day(2023, 17)
class Day17Solver : BaseSolver(), ISolver {
    enum class Direction {
        Right,
        Down,
        Up,
        Left,
    }

    companion object {
        const val PART1_MIN = 0
        const val PART1_MAX = 3
        const val PART2_MIN = 4
        const val PART2_MAX = 10

        val inverse = mapOf(
            Direction.Up to Direction.Down,
            Direction.Right to Direction.Left,
            Direction.Down to Direction.Up,
            Direction.Left to Direction.Right
        )
        val dirToArray = mapOf(
            Direction.Up to intArrayOf(0, -1),
            Direction.Right to intArrayOf(1, 0),
            Direction.Down to intArrayOf(0, 1),
            Direction.Left to intArrayOf(-1, 0)
        )

    }


    data class Pos(val x: Int, val y: Int) {
        operator fun plus(direction: IntArray): Pos {
            if (direction.size != 2) {
                throw Error("Invalid IntArray")
            }
            return Pos(
                x + direction[0],
                y + direction[1]
            )
        }

        fun isValid(maxX: Int, maxY: Int) = x in 0..maxX && y in 0..maxY
    }


    class MoveNode(val node: Node, val heatLoss: Int) : Comparable<MoveNode> {
        private val weightValue = heatLoss * 1000 + node.pos.y * 100 + node.pos.x

        fun neighbors(max: Pos, mapData: Array<IntArray>, minDirection: Int, maxDirection: Int): List<MoveNode> {
            val result = mutableListOf<MoveNode>()
            for (dir in if (node.directionTimes < minDirection) listOf(node.direction) else Direction.entries) {
                if (dir == inverse[node.direction] || (dir == node.direction && node.directionTimes == maxDirection)) {
                    continue
                }

                val nPos = node.pos + dirToArray[dir]!!

                if (nPos.isValid(max.x, max.y)) {
                    result.add(
                        MoveNode(
                            Node(nPos, dir, if (dir == node.direction) node.directionTimes + 1 else 1),
                            heatLoss + mapData[nPos.y][nPos.x]
                        )
                    )
                }
            }

            return result
        }

        override fun compareTo(other: MoveNode) = weightValue.compareTo(other.weightValue)
    }


    data class Node(val pos: Pos, val direction: Direction, val directionTimes: Int)

    private fun findDistance(objective: Pos, mapData: Array<IntArray>, minDirection: Int, maxDirection: Int): Int {
        val queue: Queue<MoveNode> = PriorityQueue()
        val visited = HashSet<Node>()

        queue.add(MoveNode(Node(Pos(1, 0), Direction.Right, 1), mapData[0][1]))
        queue.add(MoveNode(Node(Pos(0, 1), Direction.Down, 1), mapData[1][0]))

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (visited.contains(node.node)) {
                continue
            }

            visited.add(node.node)

            if (node.node.pos == objective) {
                if (node.node.directionTimes < minDirection) {
                    continue
                } else {
                    return node.heatLoss
                }
            }

            queue.addAll(node.neighbors(objective, mapData, minDirection, maxDirection))
        }

        return -1
    }


    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val maxY = lines.lastIndex - 1
        val maxX = lines[0].lastIndex

        val blockMap = Array(maxY + 1) { IntArray(maxX + 1) }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                blockMap[y][x] = lines[y][x].digitToInt()
            }
        }

        val part1 = findDistance(Pos(maxX, maxY), blockMap, PART1_MIN, PART1_MAX)
        val part2 = findDistance(Pos(maxX, maxY), blockMap, PART2_MIN, PART2_MAX)

        return Pair(part1.toString(), part2.toString())
    }
}