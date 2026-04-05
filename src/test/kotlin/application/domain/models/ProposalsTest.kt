package application.domain.models

import application.domain.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProposalsTest {

    @Test
    fun `add deve adicionar proposta a lista`() {
        val proposals = Proposals()
        val proposal = TestFixtures.proposal()
        proposals.add(proposal)
        assertEquals(proposal, proposals.getById(proposal.proposalId))
    }

    @Test
    fun `getByStatus deve retornar proposta com status correspondente`() {
        val proposals = Proposals()
        val proposal = TestFixtures.proposal(status = ProposalStatus.ACCEPTED)
        proposals.add(proposal)
        assertEquals(proposal, proposals.getByStatus(ProposalStatus.ACCEPTED))
    }

    @Test
    fun `getByStatus deve retornar null quando nenhuma proposta tem o status`() {
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal(status = ProposalStatus.SUGGESTED))
        assertNull(proposals.getByStatus(ProposalStatus.ACCEPTED))
    }

    @Test
    fun `getById deve retornar proposta com id correspondente`() {
        val proposals = Proposals()
        val proposalId = TestFixtures.proposalId()
        val proposal = TestFixtures.proposal(proposalId = proposalId)
        proposals.add(proposal)
        assertEquals(proposal, proposals.getById(proposalId))
    }

    @Test
    fun `getById deve retornar null quando id nao existe`() {
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal())
        assertNull(proposals.getById(TestFixtures.proposalId()))
    }

    @Test
    fun `accept deve marcar somente a proposta alvo como ACCEPTED`() {
        val targetId = TestFixtures.proposalId()
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal(proposalId = targetId, type = LoanType.PERSONAL))
        proposals.add(TestFixtures.proposal(type = LoanType.PAYROLL))

        val updated = proposals.accept(targetId)
        assertEquals(ProposalStatus.ACCEPTED, updated.getById(targetId)!!.status)
    }

    @Test
    fun `accept deve manter demais propostas como SUGGESTED`() {
        val targetId = TestFixtures.proposalId()
        val otherId = TestFixtures.proposalId()
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal(proposalId = targetId, type = LoanType.PERSONAL))
        proposals.add(TestFixtures.proposal(proposalId = otherId, type = LoanType.PAYROLL))

        val updated = proposals.accept(targetId)
        assertEquals(ProposalStatus.SUGGESTED, updated.getById(otherId)!!.status)
    }

    @Test
    fun `accept deve ser imutavel retornando nova instancia`() {
        val proposalId = TestFixtures.proposalId()
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal(proposalId = proposalId))

        val updated = proposals.accept(proposalId)
        assertNotSame(proposals, updated)
        assertEquals(ProposalStatus.SUGGESTED, proposals.getById(proposalId)!!.status)
    }

    @Test
    fun `iterator deve permitir iteracao sobre as propostas`() {
        val proposals = Proposals()
        proposals.add(TestFixtures.proposal(type = LoanType.PERSONAL))
        proposals.add(TestFixtures.proposal(type = LoanType.PAYROLL))

        val types = proposals.map { it.type }
        assertTrue(types.contains(LoanType.PERSONAL))
        assertTrue(types.contains(LoanType.PAYROLL))
    }
}
