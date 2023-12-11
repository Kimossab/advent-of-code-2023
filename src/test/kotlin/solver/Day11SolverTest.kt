package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11SolverTest {
    val testInput = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
"""

    @Test
    fun `should return 374 for the default input for part 1`() {
        val solver = spyk<Day11Solver>()
        every { solver.getInput(any()) } returns testInput

        assertEquals("374", solver.solve("").first)
    }

    @Test
    fun `should return 1030 for the default input for part 2 with a difference of 10`() {
        val solver = spyk(Day11Solver(10))
        every { solver.getInput(any()) } returns testInput

        assertEquals("1030", solver.solve("").second)
    }

    @Test
    fun `should return 8410 for the default input for part 2 with a difference of 100`() {
        val solver = spyk(Day11Solver(100))
        every { solver.getInput(any()) } returns testInput

        assertEquals("8410", solver.solve("").second)
    }

}