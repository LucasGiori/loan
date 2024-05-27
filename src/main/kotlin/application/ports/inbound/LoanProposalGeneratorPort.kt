package application.ports.inbound

import application.commands.LoanProposalGeneratorCommand

interface LoanProposalGeneratorPort : UseCase<LoanProposalGeneratorCommand> {
}