package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Word(val inclusion: Array<String> = emptyArray(),
		   val exclusion: Array<String> = emptyArray()) : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (arg.isEmpty()) {
			return ParseMap(false, "Word cannot be blank")
		}

		val lowerCase = arg.toLowerCase()
		if (!inclusion.map { it.toLowerCase() }.contains(lowerCase)) {
			return ParseMap(
				false,
				"Arugment: **$arg** is not an included word\n" +
					"Included words: ${inclusion.joinToString(", ") { word -> "**$word**" }}"
			)
		}
		if (exclusion.map { it.toLowerCase() }.contains(lowerCase)) return ParseMap(false, "Argument: **$arg** cannot be used for this command")
		return ParseMap(parseValue = lowerCase)
	}
}