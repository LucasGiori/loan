package application.domain.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PersonalLoanTest {

    private val loan = PersonalLoan()

    @Test
    fun `taxa deve ser 1,5 para renda ate 2000`() {
        val tax = loan.calculateTax(Income.from(2000.0))
        assertEquals(0, BigDecimal(1.5).compareTo(tax.value))
    }

    @Test
    fun `taxa deve ser 1,3 para renda entre 2001 e 5000`() {
        val tax = loan.calculateTax(Income.from(5000.0))
        assertEquals(0, BigDecimal(1.3).compareTo(tax.value))
    }

    @Test
    fun `taxa deve ser 1,1 para renda entre 5001 e 10000`() {
        val tax = loan.calculateTax(Income.from(10000.0))
        assertEquals(0, BigDecimal(1.1).compareTo(tax.value))
    }

    @Test
    fun `taxa deve ser 0,9 para renda acima de 10000`() {
        val tax = loan.calculateTax(Income.from(10001.0))
        assertEquals(0, BigDecimal(0.9).compareTo(tax.value))
    }

    @Test
    fun `valor maximo deve ser 25 porcento da renda`() {
        val income = Income.from(4000.0)
        val amount = loan.calculateMaxLoanAmount(income)
        val expected = income.value.multiply(BigDecimal(0.25))
        assertEquals(0, expected.compareTo(amount.value))
    }

    @Test
    fun `validade da proposta deve ser 7 dias`() {
        assertEquals(7, loan.getProposalValidity())
    }

    @Test
    fun `loanType deve ser PERSONAL`() {
        assertEquals(LoanType.PERSONAL, loan.loanType)
    }

    @Test
    fun `deve estar disponivel para EMPLOYEE`() {
        assertTrue(loan.availableTo().contains(Profile.EMPLOYEE))
    }

    @Test
    fun `deve estar disponivel para PUBLIC_SERVER`() {
        assertTrue(loan.availableTo().contains(Profile.PUBLIC_SERVER))
    }

    @Test
    fun `deve estar disponivel para RETIREE`() {
        assertTrue(loan.availableTo().contains(Profile.RETIREE))
    }

    @Test
    fun `deve estar disponivel para SELF_EMPLOYED`() {
        assertTrue(loan.availableTo().contains(Profile.SELF_EMPLOYED))
    }

    @Test
    fun `deve estar disponivel para PENSIONER`() {
        assertTrue(loan.availableTo().contains(Profile.PENSIONER))
    }
}
