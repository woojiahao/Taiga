package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.createParseMap
import me.chill.embedManager
import net.dv8tion.jda.api.entities.Guild

class EmbedFieldId : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val fieldId = arg.toIntOrNull() ?: return ErrorParseMap("ID: **$arg** must be an integer")

    return createParseMap(
      embedManager.hasField(guild.id, fieldId),
      arg,
      "ID: **$arg** does not exist in the embed"
    )
  }
}