package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild

class CommandName : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap =
    if (!CommandContainer.hasCommand(arg.toLowerCase())) ArgumentParseMap(false, errMsg = "Command: **$arg** does not exist")
    else ArgumentParseMap(true, parsedValue = arg.toLowerCase())
}