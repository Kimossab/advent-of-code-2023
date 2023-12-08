package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day8SolverTest {
    val testInput = """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
"""

    val testInputPart2 = """LR

AAA = (11B, ZZZ)
11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
ZZZ = (ZZZ, ZZZ)
"""

    val solver = spyk<Day8Solver>()

    @Test
    fun `should return 6 for the default input part 1`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals("6", solver.solve("").first)
    }

    @Test
    fun `should return 6 for the default input part 2`() {
        every { solver.getInput(any()) } returns testInputPart2

        assertEquals("6", solver.solve("").second)
    }
}