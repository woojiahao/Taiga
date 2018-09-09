package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.getChannel
import me.chill.database.operations.hasSuggestion
import me.chill.database.states.TargetChannel
import net.dv8tion.jda.core.entities.Guild

class SuggestionId : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		if (!hasSuggestion(guild.id, arg)) {
			return ArgumentParseMap(false, "Suggestion ID: **$arg** is not found")
		}

		val suggestionChannel = guild.getTextChannelById(getChannel(TargetChannel.Suggestion, guild.id))
		if (suggestionChannel.getMessageById(arg) == null) {
			return ArgumentParseMap(false, "Unable to find suggestion ID: **$arg** in suggestions channel")
		}

		return ArgumentParseMap(true, parsedValue = arg)
	}
}