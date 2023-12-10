package solver

import Day


@Day(2023, 10)
class Day10Solver : BaseSolver(), ISolver {
    companion object {
        fun getNextPipe(direction: Direction, position: Pair<Int, Int>): Pair<Int, Int> {
            return when (direction) {
                Direction.Up -> Pair(position.first, position.second - 1)
                Direction.Down -> Pair(position.first, position.second + 1)
                Direction.Left -> Pair(position.first - 1, position.second)
                Direction.Right -> Pair(position.first + 1, position.second)
            }
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

    override fun solve(cookie: String?): Pair<String, String?> {

        val lines = getLines(cookie)

        var start = Pair(0, 0)
        val pipeMap = lines.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val type = PipeType.entries.firstOrNull { it.char == char }
                if (type == PipeType.Start) {
                    start = Pair(x, y)
                }
                type
            }
        }

        var part1 = 0
        var part2 = 0
        var vertices: MutableList<Pair<Int, Int>> = mutableListOf()

        for (dir in listOf(Direction.Up, Direction.Right, Direction.Down, Direction.Left)) {
            var direction = dir
            var distance = 0
            var pos = start
            var area = 0
            var lastVertice: Pair<Int, Int> = start

            vertices = mutableListOf()

            while (true) {
                val n = getNextPipe(direction, pos)

                val nextDirection = when (pipeMap[n.second][n.first]) {
                    PipeType.Start -> {
                        if (dir != direction) {
                            vertices.add(n)
                        }
                        area += lastVertice.first * n.second - n.first * lastVertice.second
                        break
                    }

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

                    PipeType.SquareDownRight -> {
                        vertices.add(n)
                        area += lastVertice.first * n.second - n.first * lastVertice.second
                        lastVertice = n
                        when (direction) {
                            Direction.Down -> Direction.Right
                            Direction.Left -> Direction.Up
                            else -> null
                        }
                    }

                    PipeType.SquareDownLeft -> {
                        vertices.add(n)
                        area += lastVertice.first * n.second - n.first * lastVertice.second
                        lastVertice = n
                        when (direction) {
                            Direction.Down -> Direction.Left
                            Direction.Right -> Direction.Up
                            else -> null
                        }
                    }

                    PipeType.SquareUpLeft -> {
                        vertices.add(n)
                        area += lastVertice.first * n.second - n.first * lastVertice.second
                        lastVertice = n
                        when (direction) {
                            Direction.Up -> Direction.Left
                            Direction.Right -> Direction.Down
                            else -> null
                        }
                    }

                    PipeType.SquareUpRight -> {
                        vertices.add(n)
                        area += lastVertice.first * n.second - n.first * lastVertice.second
                        lastVertice = n
                        when (direction) {
                            Direction.Up -> Direction.Right
                            Direction.Left -> Direction.Down
                            else -> null
                        }
                    }

                    else -> null
                }

                if (nextDirection == null) {
                    distance = 0
                    break
                }

                distance++
                pos = n
                direction = nextDirection
            }

            if (distance > 0) {
                part1 = (distance + 1) / 2
                part2 = if (area < 0) {
                    area / -2 - (part1 - 1)
                } else {
                    area / 2 - (part1 - 1)
                }
                break
            }
        }

        return Pair(part1.toString(), part2.toString())
    }
}