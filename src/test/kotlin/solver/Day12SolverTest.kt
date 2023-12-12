package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12SolverTest {
    val testInput = """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
"""

    val solver = spyk<Day12Solver>()

    @Test
    fun `should return 21 as 525152 for the default input`() {
        every { solver.getInput(any()) } returns testInput

        assertEquals(Pair("21", "525152"), solver.solve(""))
    }
}