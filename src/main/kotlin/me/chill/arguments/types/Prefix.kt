package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class Prefix : Argument {
  override fun check(guild: Guild, arg: String) =
    when {
      arg.length > 3 -> ArgumentParseMap(false, "Prefix should be less than 3 characters")
      arg.all { it.isLetterOrDigit() } -> ArgumentParseMap(false, "Prefix cannot be all letters or digits")
      else -> ArgumentParseMap(true, parsedValue = arg)
    }
}