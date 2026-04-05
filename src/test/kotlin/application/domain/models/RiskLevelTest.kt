package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiskLevelTest {

    @Test
    fun `PERSONAL deve mapear para HIGH`() {
        assertEquals(RiskLevel.HIGH, RiskLevel.from(LoanType.PERSONAL))
    }

    @Test
    fun `PAYROLL deve mapear para MEDIUM`() {
        assertEquals(RiskLevel.MEDIUM, RiskLevel.from(LoanType.PAYROLL))
    }

    @Test
    fun `SECURED deve mapear para LOW`() {
        assertEquals(RiskLevel.LOW, RiskLevel.from(LoanType.SECURED))
    }
}
