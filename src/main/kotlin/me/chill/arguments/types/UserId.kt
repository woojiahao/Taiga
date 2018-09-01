package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import me.chill.utility.findUser
import net.dv8tion.jda.core.entities.Guild

class UserId(val globalSearch: Boolean = false) : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		var userId = arg
		if (arg.startsWith("<@") && arg.endsWith(">")) userId = arg.substring(2, arg.length - 1)

		if (userId.toLongOrNull() == null) {
			return ParseMap(false, "ID: **$userId** is not valid")
		}

		if (globalSearch) {
			if (guild.jda.findUser(userId) == null) {
				return ParseMap(false, "User by the ID of **$userId** does not exist on Discord")
			}
		} else {
			if (guild.getMemberById(userId) == null) {
				return ParseMap(false, "Member by the ID of **$userId** is not found")
			}
		}

		return ParseMap(parseValue = userId)
	}
}