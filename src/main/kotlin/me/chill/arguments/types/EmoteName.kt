package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class EmoteName : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		val emoteInAnyServer = guild.jda.guilds.any { it.getEmotesByName(arg, true).size > 0 }
		return if (!emoteInAnyServer) {
			ArgumentParseMap(false, "Emote: **$arg** does not exist")
		} else {
			val foundEmote = guild.jda.guilds
				.filter { it.getEmotesByName(arg, true).size > 0 }[0]
				.getEmotesByName(arg, true)[0].id
			print(foundEmote)
			ArgumentParseMap(true, parsedValue = foundEmote)
		}
	}
}
