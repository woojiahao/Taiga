package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild

class Sentence(private val maxLength: Int? = null) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (arg.isBlank())
      return ErrorParseMap("Sentence cannot be blank")

    maxLength?.let {
      if (arg.length > maxLength)
        return ErrorParseMap("Sentence cannot exceed **$it** characters")
    }

    return SuccessParseMap(arg)
  }
}