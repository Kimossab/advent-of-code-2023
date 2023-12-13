package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13SolverTest {
    val testInput = """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""

    val solver = spyk<Day13Solver>()

    @Test
    fun `should return 405 and 400 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("405", "400"), solver.solve(""))
    }
}