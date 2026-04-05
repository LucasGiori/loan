package application.domain.aggregate

import application.domain.TestFixtures
import application.domain.models.*
import application.domain.models.aggregate.RequestedLoan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class RequestedLoanTest {

    private fun loanWithAcceptedProposal(
        amount: Double = 1000.0,
        tax: Double = 1.3
    ): Pair<Proposal, RequestedLoan> {
        val proposalId = TestFixtures.proposalId()
        val proposal = TestFixtures.proposal(
            proposalId = proposalId,
            status = ProposalStatus.ACCEPTED,
            amount = amount,
            tax = tax
        )
        val proposals = Proposals().apply { add(proposal) }
        val loan = RequestedLoan(
            version = Version(3),
            identity = TestFixtures.loanId(),
            proposals = proposals
        )
        return proposal to loan
    }

    @Test
    fun `approve deve retornar LoanApprovedEvent com loanId correto`() {
        val (_, loan) = loanWithAcceptedProposal()
        val event = loan.approve()
        assertEquals(loan.identity, event.loanId)
    }

    @Test
    fun `approve deve incluir amount da proposta aceita no evento`() {
        val (proposal, loan) = loanWithAcceptedProposal(amount = 1500.0)
        val event = loan.approve()
        assertEquals(0, proposal.amount.value.compareTo(event.amount.value))
    }

    @Test
    fun `approve deve incluir tax da proposta aceita no evento`() {
        val (proposal, loan) = loanWithAcceptedProposal(tax = 1.3)
        val event = loan.approve()
        assertEquals(0, proposal.tax.value.compareTo(event.tax.value))
    }

    @Test
    fun `approve deve incrementar a versao`() {
        val (_, loan) = loanWithAcceptedProposal()
        val event = loan.approve()
        assertEquals(Version(4), event.version)
    }

    @Test
    fun `approve deve lancar IllegalStateException quando nao ha proposta aceita`() {
        val proposals = Proposals().apply {
            add(TestFixtures.proposal(status = ProposalStatus.SUGGESTED))
        }
        val loan = RequestedLoan(
            version = Version(3),
            identity = TestFixtures.loanId(),
            proposals = proposals
        )
        assertThrows<IllegalStateException> {
            loan.approve()
        }
    }

    @Test
    fun `decline deve retornar LoanDeclinedEvent com loanId correto`() {
        val (_, loan) = loanWithAcceptedProposal()
        val event = loan.decline()
        assertEquals(loan.identity, event.loanId)
    }

    @Test
    fun `decline deve incrementar a versao`() {
        val (_, loan) = loanWithAcceptedProposal()
        val event = loan.decline()
        assertEquals(Version(4), event.version)
    }
}
