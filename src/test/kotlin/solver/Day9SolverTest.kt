package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day9SolverTest {
    val testInput = """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
"""

    val solver = spyk<Day9Solver>()

    @Test
    fun `should return 114 and 2 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("114", "2"), solver.solve(""))
    }

}