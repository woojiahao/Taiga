package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class CommandName : Argument {
  override fun check(guild: Guild, arg: String) =
    if (!CommandContainer.hasCommand(arg.toLowerCase()))
      ErrorParseMap("Command: **$arg** does not exist")
    else
      SuccessParseMap(arg)
}