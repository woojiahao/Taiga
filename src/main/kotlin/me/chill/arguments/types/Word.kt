package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild

class Word(
  val inclusion: List<String> = emptyList(),
  val exclusion: List<String> = emptyList()
) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (arg.isEmpty())
      return ErrorParseMap("Word cannot be blank")

    val lowerCase = arg.toLowerCase()
    with(inclusion) {
      val notWithinInclusionList = isNotEmpty() && !map { it.toLowerCase() }.contains(lowerCase)
      if (notWithinInclusionList) {
        val errMsg = listOf(
          "Argument: **$arg** is not an included word",
          "Included words: ${joinToString(", ") { word -> "**$word**" }}"
        )
        return ErrorParseMap(errMsg.joinToString("\n"))
      }
    }

    with(exclusion) {
      val withinExclusionList = isNotEmpty() && map { it.toLowerCase() }.contains(lowerCase)
      if (withinExclusionList) {
        return ErrorParseMap("Argument: **$arg** cannot be used for this command")
      }
    }

    return SuccessParseMap(lowerCase)
  }
}