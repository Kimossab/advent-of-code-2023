package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20SolverTest {
    val testInput = """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
"""
    val testInput2 = """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
"""

    val solver = spyk<Day20Solver>()

    @Test
    fun `should return 32000000 for part 1 with the input 1`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals("32000000", solver.solve("").first)
    }

    @Test
    fun `should return 11687500 for part1 with the input 2`() {
        every { solver.getInput(any()) } returns testInput2

        assertEquals("11687500", solver.solve("").first)
    }
}