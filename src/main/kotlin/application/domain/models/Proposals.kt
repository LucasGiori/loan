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

    fun update(proposalId: ProposalId, update: (Proposal) -> Proposal) {
        val index = proposals.indexOfFirst { it.proposalId == proposalId }
        if (index != -1) {
            proposals[index] = update(proposals[index])
        }
    }

    override fun iterator(): Iterator<Proposal> {
        return proposals.iterator()
    }
}