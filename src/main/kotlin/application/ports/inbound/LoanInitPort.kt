package application.ports.inbound

import application.commands.LoanInitCommand

interface LoanInitPort : UseCase<LoanInitCommand>