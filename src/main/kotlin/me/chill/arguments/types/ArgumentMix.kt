package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

/**
 * Represents multi-args for the same position in the argument list
 * All arguments passed must be unique otherwise, an exception will be thrown
 */
class ArgumentMix(private val failureReason: String, vararg val arguments: Argument) : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		var parseMap = ArgumentParseMap(false)
		var hasMatch = false
		for (argument in arguments) {
			val temp = argument.check(guild, arg)
			hasMatch = hasMatch || temp.status
			parseMap = temp
			if (hasMatch) break
		}

		if (!parseMap.status) return ArgumentParseMap(false, failureReason)

		return parseMap
	}
}