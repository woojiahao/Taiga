package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class Prefix : Argument {
	override fun check(guild: Guild, arg: String) =
		if (arg.length > 3) ArgumentParseMap(false, "Prefix should be less than 3 characters")
		else ArgumentParseMap(true, parsedValue = arg)
}