package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15SolverTest {
    val testInput = """rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
"""

    val solver = spyk<Day15Solver>()

    @Test
    fun `should return 1320 and 145 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("1320", "145"), solver.solve(""))
    }
}