package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import me.chill.database.operations.hasSuggestion
import me.chill.database.states.TargetChannel
import net.dv8tion.jda.api.entities.Guild

class SuggestionId : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (!hasSuggestion(guild.id, arg))
      return ErrorParseMap("Suggestion ID: **$arg** is not found")

    val suggestionChannel = guild.getTextChannelById(TargetChannel.SUGGESTION.get(guild.id))
    suggestionChannel.retrieveMessageById(arg)
        ?: return ErrorParseMap("Unable to find suggestion ID: **$arg** in suggestions channel")

    return SuccessParseMap(arg)
  }
}