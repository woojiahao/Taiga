package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class ArgumentList(private val argumentType: Argument,
				   private val pipes: Char = ',',
				   private val limit: Int = -1) : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		val parsedValues = mutableListOf<String>()

		if (limit != -1 && arg.split(pipes).size > limit) {
			return ArgumentParseMap(false, "Cannot have more than **$limit** element(s) in the argument list")
		}

		for (a in arg.split(pipes)) {
			val individualCheckMap = argumentType.check(guild, a)
			if (!individualCheckMap.status) return individualCheckMap
			parsedValues.add(individualCheckMap.parsedValue)
		}
		return ArgumentParseMap(true, parsedValue = parsedValues.joinToString(pipes.toString()))
	}
}