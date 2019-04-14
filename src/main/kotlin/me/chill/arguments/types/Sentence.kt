package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

class Sentence(private val maxLength: Int? = null) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (arg.isBlank())
      return ArgumentParseMap(false, "Sentence cannot be blank")

    maxLength?.let {
      if (arg.length > maxLength)
        return ArgumentParseMap(false, "Sentence cannot exceed **$it** characters")
    }

    return ArgumentParseMap(true, parsedValue = arg)
  }
}