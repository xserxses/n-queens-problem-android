package com.github.xserxses.nqueensproblem.utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ColumnUtilsTest {

    @Test
    fun `getExcelColumnName with 0 returns A`() {
        assertEquals("A", getExcelColumnName(0))
    }

    @Test
    fun `getExcelColumnName with 25 returns Z`() {
        assertEquals("Z", getExcelColumnName(25))
    }

    @Test
    fun `getExcelColumnName with 26 returns AA`() {
        assertEquals("AA", getExcelColumnName(26))
    }

    @Test
    fun `getExcelColumnName with 27 returns AB`() {
        assertEquals("AB", getExcelColumnName(27))
    }

    @Test
    fun `getExcelColumnName with 51 returns AZ`() {
        assertEquals("AZ", getExcelColumnName(51))
    }

    @Test
    fun `getExcelColumnName with 52 returns BA`() {
        assertEquals("BA", getExcelColumnName(52))
    }

    @Test
    fun `getExcelColumnName with 701 returns ZZ`() {
        assertEquals("ZZ", getExcelColumnName(701))
    }

    @Test
    fun `getExcelColumnName with 702 returns AAA`() {
        assertEquals("AAA", getExcelColumnName(702))
    }

    @Test
    fun `getExcelColumnName with negative index throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            getExcelColumnName(-1)
        }
    }
}
