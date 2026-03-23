package application.ports.inbound

import application.commands.LoanDeclineCommand

interface LoanDeclinePort : UseCase<LoanDeclineCommand>
