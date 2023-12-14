package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14SolverTest {
    val testInput = """O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
"""

    val solver = spyk<Day14Solver>()

    @Test
    fun `should return 136 and 64 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("136", "64"), solver.solve(""))
    }
}