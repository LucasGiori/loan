package application.domain.aggregate

import application.domain.TestFixtures
import application.domain.models.*
import application.domain.models.aggregate.InitializedLoan
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InitializedLoanTest {

    private fun loan(profile: Profile = Profile.EMPLOYEE, income: Double = 5000.0) = InitializedLoan(
        customer = TestFixtures.customer(profile = profile, income = income),
        version = Version.INITIAL,
        identity = TestFixtures.loanId()
    )

    @Test
    fun `issueProposals deve retornar LoanProposalsIssuedEvent com loanId correto`() {
        val loan = loan()
        val event = loan.issueProposals()
        assertEquals(loan.identity, event.loanId)
    }

    @Test
    fun `issueProposals deve incrementar a versao`() {
        val event = loan().issueProposals()
        assertEquals(Version(2), event.version)
    }

    @Test
    fun `issueProposals deve gerar 3 propostas para EMPLOYEE`() {
        val event = loan(profile = Profile.EMPLOYEE).issueProposals()
        val types = event.proposals.map { it.type }
        assertEquals(3, types.size)
        assertTrue(types.contains(LoanType.PERSONAL))
        assertTrue(types.contains(LoanType.PAYROLL))
        assertTrue(types.contains(LoanType.SECURED))
    }

    @Test
    fun `issueProposals nao deve gerar PAYROLL para SELF_EMPLOYED`() {
        val event = loan(profile = Profile.SELF_EMPLOYED).issueProposals()
        val types = event.proposals.map { it.type }
        assertEquals(2, types.size)
        assertFalse(types.contains(LoanType.PAYROLL))
    }

    @Test
    fun `issueProposals deve criar propostas com status SUGGESTED`() {
        val event = loan().issueProposals()
        event.proposals.forEach { proposal ->
            assertEquals(ProposalStatus.SUGGESTED, proposal.status)
        }
    }

    @Test
    fun `issueProposals deve calcular taxa corretamente para PersonalLoan com renda 5000`() {
        val event = loan(profile = Profile.EMPLOYEE, income = 5000.0).issueProposals()
        val personal = event.proposals.first { it.type == LoanType.PERSONAL }
        assertEquals(0, BigDecimal(1.3).compareTo(personal.tax.value))
    }

    @Test
    fun `issueProposals deve calcular amount maximo para PersonalLoan com renda 4000`() {
        val income = Income.from(4000.0)
        val event = InitializedLoan(
            customer = TestFixtures.customer(profile = Profile.EMPLOYEE, income = 4000.0),
            version = Version.INITIAL,
            identity = TestFixtures.loanId()
        ).issueProposals()
        val personal = event.proposals.first { it.type == LoanType.PERSONAL }
        val expected = income.value.multiply(BigDecimal(0.25))
        assertEquals(0, expected.compareTo(personal.amount.value))
    }

    @Test
    fun `issueProposals deve definir expiracao em 7 dias para PersonalLoan`() {
        val before = java.time.LocalDateTime.now().plusDays(6)
        val event = loan().issueProposals()
        val after = java.time.LocalDateTime.now().plusDays(8)
        val personal = event.proposals.first { it.type == LoanType.PERSONAL }
        assertTrue(personal.expiration.isAfter(before))
        assertTrue(personal.expiration.isBefore(after))
    }

    @Test
    fun `issueProposals deve definir expiracao em 30 dias para PayrollLoan`() {
        val before = java.time.LocalDateTime.now().plusDays(29)
        val event = loan().issueProposals()
        val after = java.time.LocalDateTime.now().plusDays(31)
        val payroll = event.proposals.first { it.type == LoanType.PAYROLL }
        assertTrue(payroll.expiration.isAfter(before))
        assertTrue(payroll.expiration.isBefore(after))
    }
}
