package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.createParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class CommandName : Argument {
  override fun check(guild: Guild, arg: String) =
    createParseMap(
      CommandContainer.hasCommand(arg.toLowerCase()),
      arg,
      "Command: **$arg** does not exist"
    )
}