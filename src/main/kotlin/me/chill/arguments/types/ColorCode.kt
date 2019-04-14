package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

class ColorCode : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val colorRegex = Regex("^(#|)([0-9A-F]{6})${'$'}")

    val invalidColorMap = ArgumentParseMap(false, "Hex color: **$arg** is invalid")
    if (!colorRegex.matches(arg)) return invalidColorMap

    val colorCode = colorRegex.matchEntire(arg) ?: return invalidColorMap

    return ArgumentParseMap(true, parsedValue = colorCode.groupValues[1].toInt(16).toString())
  }
}