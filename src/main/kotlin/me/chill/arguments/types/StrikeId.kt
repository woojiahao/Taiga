package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.createParseMap
import me.chill.database.operations.hasStrike
import me.chill.utility.int
import net.dv8tion.jda.api.entities.Guild

class StrikeId : Argument {
  override fun check(guild: Guild, arg: String) =
    createParseMap(
      hasStrike(guild.id, arg.int()),
      arg,
      "Strike ID: **$arg** does not exist in the server"
    )
}