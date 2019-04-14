package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import me.chill.database.operations.hasMacro
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class MacroName(private val isExisting: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (CommandContainer.hasCommand(arg))
      return ErrorParseMap("Macro: **$arg** is an existing command name")

    val guildHasMacro = hasMacro(guild.id, arg)

    return when {
      !isExisting && guildHasMacro -> ErrorParseMap("Macro: **$arg** already exists")
      isExisting && !guildHasMacro -> ErrorParseMap("Macro: **$arg** does not exist")
      else -> SuccessParseMap(arg)
    }
  }
}