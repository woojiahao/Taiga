package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import me.chill.database.operations.hasStrike
import me.chill.utility.int
import net.dv8tion.jda.api.entities.Guild

// TODO: Add utility func to create parse map
class StrikeId : Argument {
  override fun check(guild: Guild, arg: String) =
    if (!hasStrike(guild.id, arg.int()))
      ErrorParseMap("Strike ID: **$arg** does not exist in the server")
    else
      SuccessParseMap(arg)
}