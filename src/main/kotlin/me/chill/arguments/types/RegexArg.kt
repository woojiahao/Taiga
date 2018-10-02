package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class RegexArg : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		try {
			Pattern.compile(arg)
		} catch (e: PatternSyntaxException) {
			return ArgumentParseMap(false, "Regex expression: **$arg** is not valid")
		}

		return ArgumentParseMap(true, parsedValue = arg)
	}
}