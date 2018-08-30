package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class RoleIdArgument : Argument {
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