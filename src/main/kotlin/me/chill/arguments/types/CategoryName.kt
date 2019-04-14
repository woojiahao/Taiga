package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild
import org.apache.commons.lang3.text.WordUtils

class CategoryName : Argument {
  override fun check(guild: Guild, arg: String) =
    if (!CommandContainer.hasCategory(arg))
      ErrorParseMap("Category: **$arg** does not exist")
    else
      SuccessParseMap(WordUtils.capitalize(arg))
}