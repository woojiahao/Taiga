package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class UserId(val globalSearch: Boolean = false) : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		var toCheck = arg
		if (arg.startsWith("<@") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)

		if (toCheck.toLongOrNull() == null) {
			return ParseMap(false, "ID: **$toCheck** is not valid")
		}

		if (globalSearch) {
			if (guild.jda.getUserById(toCheck) == null) {
				return ParseMap(false, "User by the ID of **$toCheck** does not exist on Discord")
			}
		} else {
			if (guild.getMemberById(toCheck) == null) {
				return ParseMap(false, "Member by the ID of **$toCheck** is not found")
			}
		}

		return ParseMap(true, parseValue = toCheck)
	}
}