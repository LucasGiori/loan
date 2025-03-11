package application.domain.models.aggregate

import application.domain.events.LoanProposalsIssuedEvent
import application.domain.models.*
import application.domain.models.service.LoanAvailabilityService
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@SerialName("InitializedLoan")
data class InitializedLoan(
    val customer: Customer,
    override val version: Version,
    override val identity: LoanId
) : Loan {
    override fun issueProposals(): LoanProposalsIssuedEvent {
        val loanRulesMap = LoanRulesFactory.get()
        val availableLoanType = LoanAvailabilityService.getAvailableLoanTypes(
            customer=customer,
            loanRulesMap=loanRulesMap
        )

        val proposals = Proposals()

        availableLoanType.forEach { (loanType, loan) ->
            val proposal = Proposal(
                proposalId = ProposalId(UUIDv4.randomUUID()),
                type = loanType,
                amount = loan.calculateMaxLoanAmount(income = customer.income),
                tax = loan.calculateTax(income = customer.income),
                status = ProposalStatus.SUGGESTED,
                expiration = LocalDateTime.now().plusDays(loan.getProposalValidity().toLong())
            )

            proposals.add(proposal = proposal)
        }

        return LoanProposalsIssuedEvent(loanId = identity, version = version.next(), proposals = proposals)
    }
}