package application.domain.models.service

import application.domain.TestFixtures
import application.domain.models.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LoanAvailabilityServiceTest {

    private val loanRulesMap = LoanRulesFactory.get()

    @Test
    fun `EMPLOYEE deve ter acesso a todos os tipos de emprestimo`() {
        val customer = TestFixtures.customer(profile = Profile.EMPLOYEE)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, loanRulesMap)
        assertEquals(3, result.size)
        assertTrue(result.containsKey(LoanType.PERSONAL))
        assertTrue(result.containsKey(LoanType.PAYROLL))
        assertTrue(result.containsKey(LoanType.SECURED))
    }

    @Test
    fun `SELF_EMPLOYED nao deve ter acesso a PAYROLL`() {
        val customer = TestFixtures.customer(profile = Profile.SELF_EMPLOYED)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, loanRulesMap)
        assertEquals(2, result.size)
        assertFalse(result.containsKey(LoanType.PAYROLL))
        assertTrue(result.containsKey(LoanType.PERSONAL))
        assertTrue(result.containsKey(LoanType.SECURED))
    }

    @Test
    fun `PENSIONER deve ter acesso a todos os tipos de emprestimo`() {
        val customer = TestFixtures.customer(profile = Profile.PENSIONER)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, loanRulesMap)
        assertEquals(3, result.size)
    }

    @Test
    fun `RETIREE deve ter acesso a todos os tipos de emprestimo`() {
        val customer = TestFixtures.customer(profile = Profile.RETIREE)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, loanRulesMap)
        assertEquals(3, result.size)
    }

    @Test
    fun `PUBLIC_SERVER deve ter acesso a todos os tipos de emprestimo`() {
        val customer = TestFixtures.customer(profile = Profile.PUBLIC_SERVER)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, loanRulesMap)
        assertEquals(3, result.size)
    }

    @Test
    fun `mapa vazio de regras deve retornar lista vazia`() {
        val customer = TestFixtures.customer(profile = Profile.EMPLOYEE)
        val result = LoanAvailabilityService.getAvailableLoanTypes(customer, emptyMap())
        assertTrue(result.isEmpty())
    }
}
