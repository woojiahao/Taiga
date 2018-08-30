package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Integer(val lowerRange: Int? = null, val upperRange: Int? = null) : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		val parsed = arg.toIntOrNull() ?: return ParseMap(false, "**$arg** is not a valid integer")

		if (lowerRange != null && parsed < lowerRange) {
			return ParseMap(false, "**$arg** is lower than the lower range of **$lowerRange**")
		}

		if (upperRange != null && parsed > upperRange) {
			return ParseMap(false, "**$arg** is greater than the upper range of **$upperRange**")
		}

		return ParseMap(true, parseValue = arg)
	}
}