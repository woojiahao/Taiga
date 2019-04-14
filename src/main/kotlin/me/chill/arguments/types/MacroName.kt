package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.hasMacro
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class MacroName(private val isExisting: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (CommandContainer.hasCommand(arg))
      return ArgumentParseMap(false, "Macro: **$arg** is an existing command name")

    val guildHasMacro = hasMacro(guild.id, arg)

    return when {
      !isExisting && guildHasMacro -> ArgumentParseMap(false, "Macro: **$arg** already exists")
      isExisting && !guildHasMacro -> ArgumentParseMap(false, "Macro: **$arg** does not exist")
      else -> ArgumentParseMap(true, parsedValue = arg)
    }
  }
}