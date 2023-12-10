package solver

import Day


@Day(2023, 10)
class Day10Solver : BaseSolver(), ISolver {
    companion object {
        fun getNextPipe(pipeList: List<Pipe>, direction: Direction, position: Pair<Int, Int>): Pipe? {
            return when (direction) {
                Direction.Up -> pipeList.firstOrNull { it.position.first == position.first && it.position.second == position.second - 1 }
                Direction.Down -> pipeList.firstOrNull { it.position.first == position.first && it.position.second == position.second + 1 }
                Direction.Left -> pipeList.firstOrNull { it.position.first == position.first - 1 && it.position.second == position.second }
                Direction.Right -> pipeList.firstOrNull { it.position.first == position.first + 1 && it.position.second == position.second }
            }
        }

        fun trapezoidArea(vertices: List<Pair<Int, Int>>): Int {
            var sum = 0
            for (i in 0..vertices.lastIndex) {
                sum += if (i == vertices.lastIndex) {
                    (vertices[i].first * vertices[0].second) - (vertices[0].first * vertices[i].second)
                } else {
                    (vertices[i].first * vertices[i + 1].second) - (vertices[i + 1].first * vertices[i].second)
                }
            }

            return if (sum < 0) sum / -2 else sum / 2
        }
    }

    enum class PipeType(val char: Char) {
        Vertical('|'),
        Horizontal('-'),
        SquareDownRight('L'),
        SquareDownLeft('J'),
        SquareUpLeft('7'),
        SquareUpRight('F'),
        Start('S')
    }

    enum class Direction {
        Up,
        Down,
        Left,
        Right
    }


    data class Pipe(val type: PipeType, val position: Pair<Int, Int>)

    override fun solve(cookie: String?): Pair<String, String?> {
        val pipeMap = mutableListOf<Pipe>()

        val lines = getLines(cookie)

        for (line in 0..lines.lastIndex) {
            for (char in 0..lines[line].lastIndex) {
                val type = PipeType.entries.firstOrNull { it.char == lines[line][char] }

                if (type != null) {
                    pipeMap.add(Pipe(type, Pair(char, line)))
                }
            }
        }

        val start = pipeMap.first { it.type == PipeType.Start }
        var part1 = 0
        var path: MutableList<Pipe> = mutableListOf()

        for (dir in listOf(Direction.Up, Direction.Down, Direction.Left, Direction.Right)) {
            var direction = dir
            var distance = 0
            var pos = start.position
            path = mutableListOf()

            while (true) {
                val n = getNextPipe(pipeMap, direction, pos)

                if (n == null) {
                    distance = 0
                    break
                }

                val nextDirection = when (n.type) {
                    PipeType.Start -> break

                    PipeType.Vertical -> when (direction) {
                        Direction.Up -> Direction.Up
                        Direction.Down -> Direction.Down
                        else -> null
                    }

                    PipeType.Horizontal -> when (direction) {
                        Direction.Left -> Direction.Left
                        Direction.Right -> Direction.Right
                        else -> null
                    }

                    PipeType.SquareDownRight -> when (direction) {
                        Direction.Down -> Direction.Right
                        Direction.Left -> Direction.Up
                        else -> null
                    }

                    PipeType.SquareDownLeft -> when (direction) {
                        Direction.Down -> Direction.Left
                        Direction.Right -> Direction.Up
                        else -> null
                    }

                    PipeType.SquareUpLeft -> when (direction) {
                        Direction.Up -> Direction.Left
                        Direction.Right -> Direction.Down
                        else -> null
                    }

                    PipeType.SquareUpRight -> when (direction) {
                        Direction.Up -> Direction.Right
                        Direction.Left -> Direction.Down
                        else -> null
                    }
                }

                if (nextDirection == null) {
                    distance = 0
                    break
                }

                distance++
                path.add(n)
                pos = n.position
                direction = nextDirection
            }

            if (distance > 0) {
                part1 = (distance + 1) / 2
                break
            }
        }

        val isStartAVertice = path.first().type != path.last().type

        val vertices = path.filter { it.type != PipeType.Horizontal && it.type != PipeType.Vertical }.toMutableList()
        if (isStartAVertice) {
            vertices.add(start)
        }

        val area = trapezoidArea(vertices.map { it.position })
        val part2 = area - (part1 - 1)
        return Pair(part1.toString(), part2.toString())
    }
}