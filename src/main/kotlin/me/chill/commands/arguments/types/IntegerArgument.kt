package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class IntegerArgument : Argument {
	override fun check(guild: Guild, arg: String) =
		if (arg.toIntOrNull() == null) {
			ParseMap(false, "**$arg** is not a valid integer")
		} else {
			ParseMap(true, parseValue = arg)
		}
}