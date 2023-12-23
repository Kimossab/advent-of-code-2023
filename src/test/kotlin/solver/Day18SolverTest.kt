package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18SolverTest {
    val testInput = """R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
"""

    val solver = spyk<Day18Solver>()

    @Test
    fun `should return 62 and 952408144115 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("62", "952408144115"), solver.solve(""))
    }
}