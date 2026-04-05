package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoanTypeTest {

    @Test
    fun `from deve retornar PERSONAL`() {
        assertEquals(LoanType.PERSONAL, LoanType.from("PERSONAL"))
    }

    @Test
    fun `from deve retornar PAYROLL`() {
        assertEquals(LoanType.PAYROLL, LoanType.from("PAYROLL"))
    }

    @Test
    fun `from deve retornar SECURED`() {
        assertEquals(LoanType.SECURED, LoanType.from("SECURED"))
    }

    @Test
    fun `from deve lancar IllegalArgumentException para valor invalido`() {
        assertThrows<IllegalArgumentException> {
            LoanType.from("INVALID")
        }
    }

    @Test
    fun `from deve ser case sensitive`() {
        assertThrows<IllegalArgumentException> {
            LoanType.from("personal")
        }
    }
}
