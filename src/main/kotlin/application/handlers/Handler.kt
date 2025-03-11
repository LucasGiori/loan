package application.handlers

import application.commands.Command

interface Handler<T: Command> {
    fun execute(command: T)
}