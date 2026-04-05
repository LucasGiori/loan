package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class IncomeTest {

    @Test
    fun `deve aceitar renda minima de 0,01`() {
        val income = Income(BigDecimal.valueOf(0.01))
        assertEquals(0, BigDecimal.valueOf(0.01).compareTo(income.value))
    }

    @Test
    fun `deve aceitar renda positiva`() {
        val income = Income(BigDecimal.valueOf(5000.0))
        assertEquals(0, BigDecimal.valueOf(5000.0).compareTo(income.value))
    }

    @Test
    fun `deve lancar excecao para renda zero`() {
        assertThrows<IllegalArgumentException> {
            Income(BigDecimal.ZERO)
        }
    }

    @Test
    fun `deve lancar excecao para renda negativa`() {
        assertThrows<IllegalArgumentException> {
            Income(BigDecimal.valueOf(-1.0))
        }
    }

    @Test
    fun `from deve criar Income a partir de Double`() {
        val income = Income.from(3000.0)
        assertEquals(0, BigDecimal.valueOf(3000.0).compareTo(income.value))
    }
}
