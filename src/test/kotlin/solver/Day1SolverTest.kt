package solver

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day1SolverTest {
    val testInput = """1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet"""
    val testInput2 = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"""

    val solver = spyk<Day1Solver>()

    @Test
    fun `should return 142 for first part for the default input`() {
        every { solver.getInput(any()) } returns testInput

        val result = solver.solve("")
        assertEquals("142", result.first)
    }

    @Test
    fun `should return 281 for first part for the default input`() {
        every { solver.getInput(any()) } returns testInput2

        val result = solver.solve("")
        assertEquals("281", result.second)
    }
}