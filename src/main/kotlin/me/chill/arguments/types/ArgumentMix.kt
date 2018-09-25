package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.exception.ArgumentException
import net.dv8tion.jda.core.entities.Guild

class ArgumentMix(vararg val arguments: Argument) : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		if (arguments.distinct().size != arguments.size) {
			throw ArgumentException("ArgumentMix", "Cannot have the same argument type in an argument mix")
		}

		var parseMap = ArgumentParseMap(false)
		var hasMatch = false
		for (argument in arguments) {
			val temp = argument.check(guild, arg)
			hasMatch = hasMatch || temp.status
			parseMap = temp
			if (hasMatch) break
		}

		return parseMap
	}
}