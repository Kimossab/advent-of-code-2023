package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16SolverTest {
    val testInput = """.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
"""

    val solver = spyk<Day16Solver>()

    @Test
    fun `should return 46 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("46", null), solver.solve(""))
    }
}