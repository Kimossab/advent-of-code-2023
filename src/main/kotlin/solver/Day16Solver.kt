package solver

import Day


@Day(2023, 16)
class Day16Solver : BaseSolver(), ISolver {
    enum class MirrorType {
        Horizontal, Vertical, DiagonalDown, // \
        DiagonalUp // /
    }

    enum class Direction {
        Up, Right, Down, Left
    }

    data class Mirror(val x: Int, val y: Int, val type: MirrorType) {
        private val cache = mutableMapOf<Direction, Map<Direction, List<Pair<Int, Int>>>>()
        fun energized(
            size: Pair<Int, Int>, allMirrors: List<Mirror>, direction: Direction
        ): Map<Direction, List<Pair<Int, Int>>> {
            if (cache.containsKey(direction)) {
                return cache[direction]!!
            }

            val leftXRange = ((allMirrors.filter { it.x < x && it.y == y }.maxOfOrNull { it.x } ?: 0)..<x)
            val rightXRange =
                ((x + 1)..(allMirrors.filter { it.x > x && it.y == y }.minOfOrNull { it.x } ?: size.first))
            val upYRange = ((allMirrors.filter { it.y < y && it.x == x }.maxOfOrNull { it.y } ?: 0)..<y)
            val downYRange = (y + 1..(allMirrors.filter { it.y > y && it.x == x }.minOfOrNull { it.y } ?: size.second))

            val left = leftXRange.map { Pair(it, y) }
            val right = rightXRange.map { Pair(it, y) }
            val up = upYRange.map { Pair(x, it) }
            val down = downYRange.map { Pair(x, it) }

            val value = when (direction) {
                Direction.Up -> {
                    when (type) {
                        MirrorType.Horizontal -> mapOf(Direction.Left to left, Direction.Right to right)
                        MirrorType.Vertical -> mapOf(Direction.Up to up)
                        MirrorType.DiagonalDown -> mapOf(Direction.Left to left)
                        MirrorType.DiagonalUp -> mapOf(Direction.Right to right)
                    }
                }

                Direction.Right -> {
                    when (type) {
                        MirrorType.Horizontal -> mapOf(Direction.Right to right)
                        MirrorType.Vertical -> mapOf(Direction.Up to up, Direction.Down to down)
                        MirrorType.DiagonalDown -> mapOf(Direction.Down to down)
                        MirrorType.DiagonalUp -> mapOf(Direction.Up to up)
                    }
                }

                Direction.Down -> {
                    when (type) {
                        MirrorType.Horizontal -> mapOf(Direction.Right to right, Direction.Left to left)
                        MirrorType.Vertical -> mapOf(Direction.Down to down)
                        MirrorType.DiagonalDown -> mapOf(Direction.Right to right)
                        MirrorType.DiagonalUp -> mapOf(Direction.Left to left)
                    }
                }

                Direction.Left -> {
                    when (type) {
                        MirrorType.Horizontal -> mapOf(Direction.Left to left)
                        MirrorType.Vertical -> mapOf(Direction.Up to up, Direction.Down to down)
                        MirrorType.DiagonalDown -> mapOf(Direction.Up to up)
                        MirrorType.DiagonalUp -> mapOf(Direction.Down to down)
                    }
                }
            }

            cache[direction] = value

            return value
        }

        override fun toString(): String {
            return "$x,$y|$type"
        }

    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val mirrors = mutableListOf<Mirror>()

        for (y in 0..lines.lastIndex) {
            for (x in 0..lines[y].lastIndex) {
                when (lines[y][x]) {
                    '|' -> mirrors.add(Mirror(x, y, MirrorType.Vertical))
                    '-' -> mirrors.add(Mirror(x, y, MirrorType.Horizontal))
                    '\\' -> mirrors.add(Mirror(x, y, MirrorType.DiagonalDown))
                    '/' -> mirrors.add(Mirror(x, y, MirrorType.DiagonalUp))
                }
            }
        }

        val size = Pair(lines.lastIndex - 1, lines[0].lastIndex)

        var part1 = 0
        var part2 = 0

        val totalStarts = mutableListOf<Pair<Pair<Int, Int>, Direction>>()

        for (y in 0..size.second) {
            totalStarts.add(Pair(Pair(0, y), Direction.Right))
            totalStarts.add(Pair(Pair(size.first, y), Direction.Left))
        }

        for (x in 0..size.first) {
            totalStarts.add(Pair(Pair(x, 0), Direction.Down))
            totalStarts.add(Pair(Pair(x, size.second), Direction.Up))
        }


        for (start in totalStarts) {
            val path = mutableSetOf<Pair<Int, Int>>()
            var firstMirror: Mirror? = null
            when (start.second) {
                Direction.Up -> {
                    firstMirror = mirrors.sortedBy { it.y }.lastOrNull { it.x == start.first.first }
                    if (firstMirror == null) {
                        continue
                    }
                    path.addAll(
                        (firstMirror.y..size.second).fold(mutableSetOf()) { acc, it ->
                            acc.add(Pair(start.first.first, it))
                            acc
                        }
                    )
                }

                Direction.Right -> {
                    firstMirror = mirrors.sortedBy { it.x }.firstOrNull { it.y == start.first.second }
                    if (firstMirror == null) {
                        continue
                    }
                    path.addAll(
                        (0..firstMirror.x).fold(mutableSetOf()) { acc, it ->
                            acc.add(Pair(it, start.first.second))
                            acc
                        }
                    )
                }

                Direction.Down -> {
                    firstMirror = mirrors.sortedBy { it.y }.firstOrNull { it.x == start.first.first }
                    if (firstMirror == null) {
                        continue
                    }
                    path.addAll(
                        (0..firstMirror.y).fold(mutableSetOf()) { acc, it ->
                            acc.add(Pair(start.first.first, it))
                            acc
                        }
                    )
                }

                Direction.Left -> {
                    firstMirror = mirrors.sortedBy { it.x }.lastOrNull { it.y == start.first.second }
                    if (firstMirror == null) {
                        continue
                    }
                    path.addAll(
                        (firstMirror.x..size.first).fold(mutableSetOf()) { acc, it ->
                            acc.add(Pair(it, start.first.second))
                            acc
                        }
                    )
                }
            }

            getPath(path, size, mirrors, mutableListOf(Pair(firstMirror, start.second)))

            if (path.size > part2) {
                part2 = path.size
            }

            if (start.first == Pair(0, 0) && start.second == Direction.Right) {
                part1 = path.size
            }
        }

        return Pair(part1.toString(), part2.toString())
    }

    private fun getPath(
        path: MutableSet<Pair<Int, Int>>,
        size: Pair<Int, Int>,
        mirrors: MutableList<Mirror>,
        initialNextPaths: List<Pair<Mirror, Direction>>
    ) {
        val visitedMirrors = mutableListOf<String>()
        val nextPaths = initialNextPaths.toMutableList()

        while (nextPaths.isNotEmpty()) {
            val p = nextPaths.first()
            nextPaths.removeAt(0)

            val pString = "${p.first}${p.second}"

            if (visitedMirrors.contains(pString)) {
                continue
            }
            visitedMirrors.add(pString)

            val np = p.first.energized(size, mirrors, p.second)

            for (d in np.keys) {
                path.addAll(np[d]!!)

                val nMirror = when (d) {
                    Direction.Up -> mirrors.filter { it.x == p.first.x && it.y < p.first.y }.maxByOrNull { it.y }
                    Direction.Right -> mirrors.filter { it.y == p.first.y && it.x > p.first.x }.minByOrNull { it.x }
                    Direction.Down -> mirrors.filter { it.x == p.first.x && it.y > p.first.y }.minByOrNull { it.y }
                    Direction.Left -> mirrors.filter { it.y == p.first.y && it.x < p.first.x }.maxByOrNull { it.x }
                }

                if (nMirror !== null) {
                    nextPaths.add(Pair(nMirror, d))
                }
            }
        }
    }
}