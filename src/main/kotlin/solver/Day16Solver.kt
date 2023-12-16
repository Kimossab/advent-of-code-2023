package solver

import Day
import kotlin.math.max
import kotlin.math.min


@Day(2023, 16)
class Day16Solver : BaseSolver(), ISolver {
    enum class MirrorType {
        Horizontal,
        Vertical,
        DiagonalDown, // \
        DiagonalUp // /
    }

    enum class Direction {
        Up,
        Right,
        Down,
        Left
    }

    data class Mirror(val x: Int, val y: Int, val type: MirrorType)

    fun getNextPos(pos: Pair<Int, Int>, dir: Direction): Pair<Int, Int> {
        return when (dir) {
            Direction.Up -> Pair(pos.first, pos.second - 1)
            Direction.Down -> Pair(pos.first, pos.second + 1)
            Direction.Right -> Pair(pos.first + 1, pos.second)
            Direction.Left -> Pair(pos.first - 1, pos.second)
        }
    }

    fun getLightPath(
        mapSize: Pair<Int, Int>,
        pos: Pair<Int, Int>,
        dir: Direction,
        mirrors: List<Mirror>,
        path: MutableSet<Pair<Pair<Int, Int>, Direction>
    ) {
        var curPos = pos
        while (true) {
            if (pos.first < 0 || pos.second < 0 || pos.first > mapSize.first || pos.second > mapSize.second) {
                return
            }

            val hasMirror = mirrors.firstOrNull { it.x == pos.first && it.y == pos.second }
            path.add(Pair(pos, dir))

            if (hasMirror == null) {
                curPos = getNextPos(pos, dir)
                continue
            }

            when (hasMirror.type) {
                MirrorType.Horizontal -> {
                    if (dir == Direction.Right || dir == Direction.Left) {
                        curPos = getNextPos(pos, dir)
                        continue
                    }

                    val p1 = getNextPos(pos, Direction.Left)
                    val p2 = getNextPos(pos, Direction.Right)
                    getLightPath(p1, Direction.Left, mirrors, path)
                    getLightPath(p2, Direction.Right, mirrors, path)
                }

                MirrorType.Vertical -> {
                    if (dir == Direction.Up || dir == Direction.Down) {
                        val p = getNextPos(pos, dir)
                        return getLightPath(p, dir, mirrors, path)
                    }

                    val p1 = getNextPos(pos, Direction.Up)
                    val p2 = getNextPos(pos, Direction.Down)
                    getLightPath(p1, Direction.Up, mirrors, path)
                    getLightPath(p2, Direction.Down, mirrors, path)
                }

                MirrorType.DiagonalUp -> {
                    when (dir) {
                        Direction.Up -> {
                            val p = getNextPos(pos, Direction.Right)
                            return getLightPath(p, Direction.Right, mirrors, path)
                        }

                        Direction.Right -> {
                            val p = getNextPos(pos, Direction.Up)
                            return getLightPath(p, Direction.Up, mirrors, path)
                        }

                        Direction.Down -> {
                            val p = getNextPos(pos, Direction.Left)
                            return getLightPath(p, Direction.Left, mirrors, path)
                        }

                        Direction.Left -> {
                            val p = getNextPos(pos, Direction.Down)
                            return getLightPath(p, Direction.Down, mirrors, path)
                        }
                    }
                }

                MirrorType.DiagonalDown -> {
                    when (dir) {
                        Direction.Up -> {
                            val p = getNextPos(pos, Direction.Left)
                            return getLightPath(p, Direction.Left, mirrors, path)
                        }

                        Direction.Right -> {
                            val p = getNextPos(pos, Direction.Down)
                            return getLightPath(p, Direction.Down, mirrors, path)
                        }

                        Direction.Down -> {
                            val p = getNextPos(pos, Direction.Right)
                            return getLightPath(p, Direction.Right, mirrors, path)
                        }

                        Direction.Left -> {
                            val p = getNextPos(pos, Direction.Up)
                            return getLightPath(p, Direction.Up, mirrors, path)
                        }
                    }
                }
            }
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

        val path = mutableSetOf<Pair<Int, Int>>()
        getLightPath(Pair(0, 0), Direction.Right, mirrors, path)

        println(path)

        return Pair(0.toString(), null)
    }
}