package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.utility.findUser
import net.dv8tion.jda.api.entities.Guild

class UserId(private val globalSearch: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    var userId = arg
    if (arg.startsWith("<@") && arg.endsWith(">")) userId = arg.substring(2, arg.length - 1)
    if (arg.startsWith("<@!") && arg.endsWith(">")) userId = arg.substring(3, arg.length - 1)

    if (userId.toLongOrNull() == null) {
      return ArgumentParseMap(false, "ID: **$userId** is not valid")
    }

    if (globalSearch) {
      if (guild.jda.findUser(userId) == null) {
        return ArgumentParseMap(false, "User by the ID of **$userId** does not exist on Discord")
      }
    } else {
      if (guild.getMemberById(userId) == null) {
        return ArgumentParseMap(false, "Member by the ID of **$userId** is not found")
      }
    }

    return ArgumentParseMap(true, parsedValue = userId)
  }
}