package driver

import application.commands.Command

interface Request {
    fun toCommand(): Command
}