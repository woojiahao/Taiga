package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class ColorCode : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val parsed = if (arg.startsWith("#")) arg.substring(1) else arg
    val hexRegex = Regex("^[0-9A-F]{6}${'$'}")

    if (!hexRegex.matches(parsed)) return ArgumentParseMap(false, "Hex color: **$arg** is invalid")

    return ArgumentParseMap(true, parsedValue = parsed.toInt(16).toString())
  }
}