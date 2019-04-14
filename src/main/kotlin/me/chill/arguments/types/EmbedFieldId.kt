package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.embedManager
import net.dv8tion.jda.api.entities.Guild

class EmbedFieldId : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val fieldId = arg.toIntOrNull() ?: return ArgumentParseMap(false, "ID: **$arg** must be an integer")

    return if (!embedManager.hasField(guild.id, fieldId))
      ArgumentParseMap(false, "ID: **$arg** does not exist in the embed")
    else
      ArgumentParseMap(true, parsedValue = arg)
  }
}