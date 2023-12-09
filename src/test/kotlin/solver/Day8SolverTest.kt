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
 

    val solver = spyk<Day8Solver>()

    @Test
    fun `should return 6 for the default input part 1`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals("6", solver.solve("").first)
    }

}