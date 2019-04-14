package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild

class Integer(
  private val lowerRange: Int? = null,
  private val upperRange: Int? = null
) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val parsed = arg.toIntOrNull() ?: return ErrorParseMap("**$arg** is not a valid integer")

    lowerRange?.let {
      if (parsed < lowerRange)
        return ErrorParseMap("**$arg** is lower than the lower range of **$lowerRange**")
    }

    upperRange?.let {
      if (parsed > upperRange)
        return ErrorParseMap("**$arg** is greater than the upper range of **$upperRange**")
    }

    return SuccessParseMap(arg)
  }
}