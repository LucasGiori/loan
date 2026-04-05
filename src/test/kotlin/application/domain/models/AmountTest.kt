package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class AmountTest {

    @Test
    fun `deve aceitar valor minimo de 0,01`() {
        val amount = Amount(BigDecimal.valueOf(0.01))
        assertEquals(0, BigDecimal.valueOf(0.01).compareTo(amount.value))
    }

    @Test
    fun `deve aceitar valor positivo`() {
        val amount = Amount(BigDecimal.valueOf(1000.0))
        assertEquals(0, BigDecimal.valueOf(1000.0).compareTo(amount.value))
    }

    @Test
    fun `deve lancar excecao para valor zero`() {
        assertThrows<IllegalArgumentException> {
            Amount(BigDecimal.ZERO)
        }
    }

    @Test
    fun `deve lancar excecao para valor negativo`() {
        assertThrows<IllegalArgumentException> {
            Amount(BigDecimal.valueOf(-1.0))
        }
    }

    @Test
    fun `from deve criar Amount a partir de Double`() {
        val amount = Amount.from(500.0)
        assertEquals(0, BigDecimal.valueOf(500.0).compareTo(amount.value))
    }
}
