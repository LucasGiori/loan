package application.ports.inbound

import application.commands.LoanApproveCommand

interface LoanApprovePort : UseCase<LoanApproveCommand>
