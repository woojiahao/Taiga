package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import me.chill.database.operations.hasSuggestion
import me.chill.database.states.TargetChannel
import net.dv8tion.jda.core.entities.Guild

class SuggestionId : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (!hasSuggestion(guild.id, arg)) {
			return ParseMap(false, "Suggestion ID: **$arg** is not found")
		}

		val suggestionChannel = guild.getTextChannelById(me.chill.database.operations.getChannel(TargetChannel.Suggestion, guild.id))
		if (suggestionChannel.getMessageById(arg) == null) {
			return ParseMap(false, "Unable to find suggestion ID: **$arg** in suggestions channel")
		}

		return ParseMap(parseValue = arg)
	}
}