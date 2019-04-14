package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild

class Prefix : Argument {
  override fun check(guild: Guild, arg: String) =
    when {
      arg.length > 3 -> ErrorParseMap("Prefix should be less than 3 characters")
      arg.all { it.isLetterOrDigit() } -> ErrorParseMap("Prefix cannot be all letters or digits")
      else -> SuccessParseMap(arg)
    }
}