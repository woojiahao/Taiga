package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

class Word(val inclusion: Array<String> = emptyArray(),
           val exclusion: Array<String> = emptyArray()) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    if (arg.isEmpty()) {
      return ArgumentParseMap(false, "Word cannot be blank")
    }

    val lowerCase = arg.toLowerCase()
    if (inclusion.isNotEmpty() && !inclusion.map { it.toLowerCase() }.contains(lowerCase)) {
      return ArgumentParseMap(
        false,
        "Arugment: **$arg** is not an included word\n" +
          "Included words: ${inclusion.joinToString(", ") { word -> "**$word**" }}"
      )
    }

    if (exclusion.isNotEmpty() && exclusion.map { it.toLowerCase() }.contains(lowerCase)) {
      return ArgumentParseMap(false, "Argument: **$arg** cannot be used for this command")
    }
    return ArgumentParseMap(true, parsedValue = lowerCase)
  }
}