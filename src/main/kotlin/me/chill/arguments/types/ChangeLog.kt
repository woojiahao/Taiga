package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild
import java.io.File

class ChangeLog : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val logNumber = arg.toIntOrNull()
        ?: return ErrorParseMap("Changelogs are in numerical form, use an integer next time")

    val hasChangelog = File("changelogs/")
      .listFiles()
      .map {
        val fileName = it.name
        fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf(".")).toInt()
      }
      .contains(logNumber)

    if (!hasChangelog)
      return ErrorParseMap("Changelog: **$arg** does not exist")

    return SuccessParseMap(parsedValue = arg)
  }
}