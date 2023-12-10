package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10SolverTest {
    val testInput1 = """.....
.S-7.
.|.|.
.L-J.
.....
"""
    val testInput2 = """..F7.
.FJ|.
SJ.L7
|F--J
LJ...
"""

    val testInput3 = """...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
"""

    val testInput4 = """.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
"""

    val testInput5 = """FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
"""

    val solver = spyk<Day10Solver>()

    @Test
    fun `should return 4 for the default input 1 for part 1`() {
        every { solver.getInput(any()) } returns testInput1

        assertEquals("4", solver.solve("").first)
    }

    @Test
    fun `should return 8 for the default input 2 for part 1`() {
        every { solver.getInput(any()) } returns testInput2

        assertEquals("8", solver.solve("").first)
    }

    @Test
    fun `should return 4 and null for the default input 3 for part 2`() {
        every { solver.getInput(any()) } returns testInput3

        assertEquals("4", solver.solve("").second)
    }

    @Test
    fun `should return 8 and null for the default input 4 for part 2`() {
        every { solver.getInput(any()) } returns testInput4

        assertEquals("8", solver.solve("").second)
    }

    @Test
    fun `should return 10 and null for the default input 5 for part 2`() {
        every { solver.getInput(any()) } returns testInput5

        assertEquals("10", solver.solve("").second)
    }

}