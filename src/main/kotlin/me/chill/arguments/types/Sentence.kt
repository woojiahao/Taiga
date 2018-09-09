package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class Sentence : Argument {
	override fun check(guild: Guild, arg: String) =
		if (arg.isBlank()) ArgumentParseMap(false, "Sentence cannot be blank")
		else ArgumentParseMap(true, parsedValue = arg)
}