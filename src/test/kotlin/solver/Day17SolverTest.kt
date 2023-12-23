package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17SolverTest {
    val testInput = """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
"""
    val testInput2 = """111111111111
999999999991
999999999991
999999999991
999999999991
"""

    val solver = spyk<Day17Solver>()

    @Test
    fun `should return 102 and 94 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("102", "94"), solver.solve(""))
    }

    @Test
    fun `should return 71 for the part2 with example 2 input`() {
        every { solver.getInput(any()) } returns testInput2

        assertEquals("71", solver.solve("").second)
    }
}