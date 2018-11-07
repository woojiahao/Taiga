package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class ChannelId : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    var toCheck = arg
    if (arg.startsWith("<#") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)

    return when {
      toCheck.toLongOrNull() == null -> ArgumentParseMap(false, "ID: **$toCheck** is not valid")
      guild.getTextChannelById(toCheck) == null -> ArgumentParseMap(false, "Channel by the ID of **$toCheck** is not found")
      else -> ArgumentParseMap(true, parsedValue = toCheck)
    }
  }
}