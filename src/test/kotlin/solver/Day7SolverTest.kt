package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7SolverTest {
    val testInput = """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
"""

    val solver = spyk<Day7Solver>()

    @Test
    fun `should return 6440 and 5905 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("6440", "5905"), solver.solve(""))
    }
}