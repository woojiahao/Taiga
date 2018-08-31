package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.core.entities.Guild

class CommandName : Argument {
	override fun check(guild: Guild, arg: String) =
		if (!CommandContainer.hasCommand(arg)) ParseMap(false, "Command: **$arg** does not exist")
		else ParseMap(parseValue = arg)
}