package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Prefix : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (arg.length > 3) return ParseMap(false, "Prefix should be less than 3 characters")
		return ParseMap(parseValue = arg)
	}
}