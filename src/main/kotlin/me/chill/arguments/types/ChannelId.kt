package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class ChannelId : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		var toCheck = arg
		if (arg.startsWith("<#") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)

		return when {
			toCheck.toLongOrNull() == null -> ParseMap(false, "ID: **$toCheck** is not valid")
			guild.getTextChannelById(toCheck) == null -> ParseMap(false, "Channel by the ID of **$toCheck** is not found")
			else -> ParseMap(parseValue = toCheck)
		}
	}
}