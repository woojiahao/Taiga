package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class ChannelIdArgument : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		var toCheck = arg
		if (arg.startsWith("<#") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)

		if (toCheck.toLongOrNull() == null) {
			return ParseMap(false, "ID: **$toCheck** is not valid")
		}

		if (guild.getTextChannelById(toCheck) == null) {
			return ParseMap(false, "Channel by the ID of **$toCheck** is not found")
		}

		return ParseMap(true, parseValue = toCheck)
	}
}