package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.utility.findUser
import net.dv8tion.jda.api.entities.Guild

class UserId(private val globalSearch: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val userIdRegex = Regex("<(@(!|))(\\d{10,})>")

    val invalidIdMap = ArgumentParseMap(false, "ID: **$arg** is not valid")

    if (!userIdRegex.matches(arg)) return invalidIdMap

    val matches = userIdRegex.matchEntire(arg) ?: return invalidIdMap

    val userId = matches.groupValues[2]

    return when {
      globalSearch && guild.jda.findUser(userId) == null ->
        ArgumentParseMap(false, "User by the ID of **$userId** does not exist on Discord")
      !globalSearch && guild.getMemberById(userId) == null ->
        ArgumentParseMap(false, "Member by the ID of **$userId** is not found")
      else -> ArgumentParseMap(true, parsedValue = userId)
    }
  }
}