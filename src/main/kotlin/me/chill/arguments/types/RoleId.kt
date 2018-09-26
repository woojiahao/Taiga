package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class RoleId : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		val rolesNoSpace = guild.roles.asSequence().map { it.name.replace(Regex("\\s+"), "").toLowerCase() }
		return when {
			rolesNoSpace.any { it == arg.toLowerCase() } ->
				ArgumentParseMap(true, parsedValue = guild.roles[rolesNoSpace.indexOf(arg)].id)
			arg.toLongOrNull() == null -> ArgumentParseMap(false, "ID: **$arg** is not valid")
			guild.getRoleById(arg) == null -> ArgumentParseMap(false, "Role by the ID of **$arg** is not found")
			else -> ArgumentParseMap(true, parsedValue = arg)
		}
	}
}