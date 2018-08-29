package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.framework.CommandContainer
import net.dv8tion.jda.core.entities.Guild

class CommandNameArgument : Argument {
	override fun check(guild: Guild, arg: String) = !CommandContainer.hasCommand(arg)
}