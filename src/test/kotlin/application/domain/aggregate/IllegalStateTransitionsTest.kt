package application.domain.aggregate

import application.domain.TestFixtures
import application.domain.exceptions.IllegalStateTransitionException
import application.domain.models.*
import application.domain.models.aggregate.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IllegalStateTransitionsTest {

    private fun initializedLoan() = InitializedLoan(
        customer = TestFixtures.customer(),
        version = Version.INITIAL,
        identity = TestFixtures.loanId()
    )

    private fun proposalsIssuedLoan() = ProposalsIssuedLoan(
        version = Version(2),
        identity = TestFixtures.loanId(),
        proposals = Proposals().apply { add(TestFixtures.proposal()) }
    )

    private fun requestedLoan() = RequestedLoan(
        version = Version(3),
        identity = TestFixtures.loanId(),
        proposals = Proposals().apply {
            add(TestFixtures.proposal(status = ProposalStatus.ACCEPTED))
        }
    )

    private fun approvedLoan() = ApprovedLoan(
        version = Version(4),
        identity = TestFixtures.loanId(),
        proposals = Proposals().apply {
            add(TestFixtures.proposal(status = ProposalStatus.ACCEPTED))
        },
        amount = Amount.from(1000.0),
        tax = Tax.from(1.3)
    )

    private fun declinedLoan() = DeclinedLoan(
        version = Version(4),
        identity = TestFixtures.loanId(),
        proposals = Proposals().apply { add(TestFixtures.proposal()) }
    )

    // InitializedLoan — transições inválidas
    @Test
    fun `InitializedLoan request deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { initializedLoan().request(TestFixtures.proposalId()) }
    }

    @Test
    fun `InitializedLoan approve deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { initializedLoan().approve() }
    }

    @Test
    fun `InitializedLoan decline deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { initializedLoan().decline() }
    }

    // ProposalsIssuedLoan — transições inválidas
    @Test
    fun `ProposalsIssuedLoan issueProposals deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { proposalsIssuedLoan().issueProposals() }
    }

    @Test
    fun `ProposalsIssuedLoan approve deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { proposalsIssuedLoan().approve() }
    }

    @Test
    fun `ProposalsIssuedLoan decline deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { proposalsIssuedLoan().decline() }
    }

    // RequestedLoan — transições inválidas
    @Test
    fun `RequestedLoan issueProposals deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { requestedLoan().issueProposals() }
    }

    @Test
    fun `RequestedLoan request deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { requestedLoan().request(TestFixtures.proposalId()) }
    }

    // ApprovedLoan — todas as transições são inválidas
    @Test
    fun `ApprovedLoan issueProposals deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { approvedLoan().issueProposals() }
    }

    @Test
    fun `ApprovedLoan request deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { approvedLoan().request(TestFixtures.proposalId()) }
    }

    @Test
    fun `ApprovedLoan approve deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { approvedLoan().approve() }
    }

    @Test
    fun `ApprovedLoan decline deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { approvedLoan().decline() }
    }

    // DeclinedLoan — todas as transições são inválidas
    @Test
    fun `DeclinedLoan issueProposals deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { declinedLoan().issueProposals() }
    }

    @Test
    fun `DeclinedLoan request deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { declinedLoan().request(TestFixtures.proposalId()) }
    }

    @Test
    fun `DeclinedLoan approve deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { declinedLoan().approve() }
    }

    @Test
    fun `DeclinedLoan decline deve lancar IllegalStateTransitionException`() {
        assertThrows<IllegalStateTransitionException> { declinedLoan().decline() }
    }
}
