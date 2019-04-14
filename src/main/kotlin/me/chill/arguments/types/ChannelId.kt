package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

class ChannelId : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val channelIdRegex = Regex("<(#|)(\\d{10,})>")

    val invalidIdMap = ArgumentParseMap(false, "ID: **$arg** is not valid")

    if (!channelIdRegex.matches(arg)) return invalidIdMap

    val matches = channelIdRegex.matchEntire(arg) ?: return invalidIdMap
    val channelId = matches.groupValues[1]
    guild.getTextChannelById(channelId)
        ?: return ArgumentParseMap(false, "Channel by ID of **$channelId** is not found")

    return ArgumentParseMap(true, parsedValue = channelId)
  }
}