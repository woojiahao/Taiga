package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

class EmoteName : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    with(guild.jda.guilds) {
      val isEmoteInServers = any { it.getEmotesByName(arg, true).size > 0 }

      return if (!isEmoteInServers) {
        ArgumentParseMap(false, "Emote: **$arg** does not exist")
      } else {
        val foundEmote = first { it.getEmotesByName(arg, true).size > 0 }.getEmotesByName(arg, true)[0].id
        ArgumentParseMap(true, parsedValue = foundEmote)
      }
    }
  }
}
