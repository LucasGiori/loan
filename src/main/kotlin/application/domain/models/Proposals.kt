package application.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Proposals(private val proposals: MutableList<Proposal> = mutableListOf()): Iterable<Proposal> {
    fun add(proposal: Proposal) {
        proposals.add(proposal)
    }

    fun getByStatus(status: ProposalStatus): Proposal? {
        return proposals.find { it.status == status }
    }

    fun getById(proposalId: ProposalId): Proposal? {
        return proposals.find { it.proposalId == proposalId }
    }

    fun accept(proposalId: ProposalId): Proposals {
        val updated = Proposals()
        proposals.forEach { proposal ->
            if (proposal.proposalId == proposalId) {
                updated.add(proposal.copy(status = ProposalStatus.ACCEPTED))
            } else {
                updated.add(proposal)
            }
        }
        return updated
    }

    override fun iterator(): Iterator<Proposal> {
        return proposals.iterator()
    }
}