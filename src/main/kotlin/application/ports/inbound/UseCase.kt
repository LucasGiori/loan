package application.ports.inbound

import application.commands.Command

sealed interface UseCase<T: Command> {
    fun execute(command: T)
}