package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class ArgumentList(private val argumentType: Argument,
				   private val pipes: Char = ',') : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		for (a in arg.split(pipes)) {
			val individualCheckMap = argumentType.check(guild, a)
			if (!individualCheckMap.status) return individualCheckMap
		}
		return ArgumentParseMap(true, parsedValue = arg)
	}
}