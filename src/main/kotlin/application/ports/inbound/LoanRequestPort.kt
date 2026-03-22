package application.ports.inbound

import application.commands.LoanRequestCommand

interface LoanRequestPort : UseCase<LoanRequestCommand>
