package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6SolverTest {
    val testInput = """Time:      7  15   30
Distance:  9  40  200
"""

    val solver = spyk<Day6Solver>()

    @Test
    fun `should return 288 and 71503 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("288", "71503"), solver.solve(""))
    }
}