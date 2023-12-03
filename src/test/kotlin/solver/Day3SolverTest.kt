package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3SolverTest {
    val testInput = """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...${'$'}.*....
.664.598..
"""

    val solver = spyk<Day3Solver>()

    @Test
    fun `should return 4361 and 467835 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("4361", "467835"), solver.solve(""))
    }
}