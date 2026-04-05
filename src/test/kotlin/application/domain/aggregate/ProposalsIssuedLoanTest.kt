package application.domain.aggregate

import application.domain.TestFixtures
import application.domain.exceptions.UnknownProposalException
import application.domain.models.*
import application.domain.models.aggregate.ProposalsIssuedLoan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProposalsIssuedLoanTest {

    private fun loanWithProposal(proposalId: ProposalId = TestFixtures.proposalId()): Pair<ProposalId, ProposalsIssuedLoan> {
        val proposals = Proposals().apply {
            add(TestFixtures.proposal(proposalId = proposalId, type = LoanType.PERSONAL))
            add(TestFixtures.proposal(type = LoanType.SECURED))
        }
        val loan = ProposalsIssuedLoan(
            version = Version(2),
            identity = TestFixtures.loanId(),
            proposals = proposals
        )
        return proposalId to loan
    }

    @Test
    fun `request deve retornar LoanRequestedEvent com proposta aceita`() {
        val (proposalId, loan) = loanWithProposal()
        val event = loan.request(proposalId)
        assertEquals(ProposalStatus.ACCEPTED, event.proposals.getById(proposalId)!!.status)
    }

    @Test
    fun `request deve incrementar a versao`() {
        val (proposalId, loan) = loanWithProposal()
        val event = loan.request(proposalId)
        assertEquals(Version(3), event.version)
    }

    @Test
    fun `request deve manter demais propostas como SUGGESTED`() {
        val targetId = TestFixtures.proposalId()
        val otherId = TestFixtures.proposalId()
        val proposals = Proposals().apply {
            add(TestFixtures.proposal(proposalId = targetId, type = LoanType.PERSONAL))
            add(TestFixtures.proposal(proposalId = otherId, type = LoanType.PAYROLL))
        }
        val loan = ProposalsIssuedLoan(
            version = Version(2),
            identity = TestFixtures.loanId(),
            proposals = proposals
        )
        val event = loan.request(targetId)
        assertEquals(ProposalStatus.SUGGESTED, event.proposals.getById(otherId)!!.status)
    }

    @Test
    fun `request deve lancar UnknownProposalException para proposalId inexistente`() {
        val (_, loan) = loanWithProposal()
        assertThrows<UnknownProposalException> {
            loan.request(TestFixtures.proposalId())
        }
    }
}
