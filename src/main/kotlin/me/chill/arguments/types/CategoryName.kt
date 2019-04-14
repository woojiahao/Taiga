package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.createParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.api.entities.Guild
import org.apache.commons.lang3.text.WordUtils

class CategoryName : Argument {
  override fun check(guild: Guild, arg: String) =
    createParseMap(
      CommandContainer.hasCategory(arg),
      WordUtils.capitalize(arg),
      "Category: **$arg** does not exist"
    )
}