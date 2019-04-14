package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class RegexArg : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    return try {
      Pattern.compile(arg)
      SuccessParseMap(arg)
    } catch (e: PatternSyntaxException) {
      ErrorParseMap("Regex expression: **$arg** is not valid")
    }
  }
}