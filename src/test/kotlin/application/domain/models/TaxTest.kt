package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class TaxTest {

    @Test
    fun `deve aceitar valor zero`() {
        val tax = Tax(BigDecimal.ZERO)
        assertEquals(0, BigDecimal.ZERO.compareTo(tax.value))
    }

    @Test
    fun `deve aceitar valor positivo`() {
        val tax = Tax(BigDecimal.valueOf(1.5))
        assertEquals(0, BigDecimal.valueOf(1.5).compareTo(tax.value))
    }

    @Test
    fun `deve lancar excecao para valor negativo`() {
        assertThrows<IllegalArgumentException> {
            Tax(BigDecimal.valueOf(-0.1))
        }
    }

    @Test
    fun `from deve criar Tax a partir de Double`() {
        val tax = Tax.from(1.3)
        assertEquals(0, BigDecimal.valueOf(1.3).compareTo(tax.value))
    }
}
