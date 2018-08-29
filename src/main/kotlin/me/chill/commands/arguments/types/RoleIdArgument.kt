package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import net.dv8tion.jda.core.entities.Guild

class RoleIdArgument : Argument {
	override fun check(guild: Guild, arg: String): Boolean {
		if (arg.toLongOrNull() == null) return false
		return guild.getRoleById(arg) != null
	}
}