package application.domain

import application.domain.models.*
import java.time.LocalDateTime

object TestFixtures {

    fun customer(
        profile: Profile = Profile.EMPLOYEE,
        income: Double = 5000.0
    ) = Customer(
        name = Name("Test User"),
        document = Document("12345678901"),
        age = Age(30),
        location = "São Paulo",
        profile = profile,
        income = Income.from(income)
    )

    fun loanId() = LoanId(UUIDv4.randomUUID())

    fun proposalId() = ProposalId(UUIDv4.randomUUID())

    fun proposal(
        proposalId: ProposalId = proposalId(),
        type: LoanType = LoanType.PERSONAL,
        status: ProposalStatus = ProposalStatus.SUGGESTED,
        amount: Double = 1000.0,
        tax: Double = 1.5
    ) = Proposal(
        proposalId = proposalId,
        type = type,
        amount = Amount.from(amount),
        tax = Tax.from(tax),
        status = status,
        expiration = LocalDateTime.now().plusDays(7)
    )
}
