package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.hasStrike
import me.chill.utility.int
import net.dv8tion.jda.api.entities.Guild

class StrikeId : Argument {
  override fun check(guild: Guild, arg: String) =
    if (!hasStrike(guild.id, arg.int()))
      ArgumentParseMap(false, "Strike ID: **$arg** does not exist in the server")
    else
      ArgumentParseMap(true, parsedValue = arg)
}