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

    override fun iterator(): Iterator<Proposal> {
        return proposals.iterator()
    }
}