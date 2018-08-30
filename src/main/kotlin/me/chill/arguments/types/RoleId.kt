package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class RoleId : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (arg.toLongOrNull() == null) {
			return ParseMap(false, "ID: **$arg** is not valid")
		}

		if (guild.getRoleById(arg) == null) {
			return ParseMap(false, "Role by the ID of **$arg** is not found")
		}

		return ParseMap(true, parseValue = arg)
	}
}