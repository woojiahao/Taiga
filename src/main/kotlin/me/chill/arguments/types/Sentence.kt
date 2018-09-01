package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Sentence : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (arg.isBlank()) {
			return ParseMap(false, "Sentence cannot be blank")
		}

		return ParseMap(parseValue = arg)
	}
}