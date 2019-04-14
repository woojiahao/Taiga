package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.createParseMap
import net.dv8tion.jda.api.entities.Guild

class EmoteName : Argument {
  override fun check(guild: Guild, arg: String) =
    with(guild.jda.guilds) {
      createParseMap(
        any { it.getEmotesByName(arg, true).size > 0 },
        first { it.getEmotesByName(arg, true).size > 0 }.getEmotesByName(arg, true)[0].id,
        "Emote: **$arg** does not exist"
      )
    }
}
