package application.domain.exceptions

import application.domain.models.ProposalId

data class UnknownProposalException(val proposalId: ProposalId) : DomainException()
