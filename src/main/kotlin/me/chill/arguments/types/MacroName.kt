package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.hasMacro
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class MacroName(private val isExisting: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (CommandContainer.hasCommand(arg)) {
      return ArgumentParseMap(false, "Macro: **$arg** is an existing command name")
    }

    if (!isExisting) {
      if (hasMacro(guild.id, arg)) {
        return ArgumentParseMap(false, "Macro: **$arg** already exists")
      }
    } else {
      if (!hasMacro(guild.id, arg)) {
        return ArgumentParseMap(false, "Macro: **$arg** does not exist")
      }
    }

    return ArgumentParseMap(true, parsedValue = arg)
  }
}