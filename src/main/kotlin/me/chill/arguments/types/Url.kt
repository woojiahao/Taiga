package me.chill.arguments.types

import me.chill.arguments.*
import net.dv8tion.jda.api.entities.Guild

class Url : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val urlRegex = Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)")

    return createParseMap(urlRegex.matches(arg), arg, "Input: **$arg** is not a valid URL")
  }
}