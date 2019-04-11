package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

/**
 * Represents multi-args for the same position in the argument list
 * All arguments passed must be unique otherwise, an exception will be thrown
 */
class ArgumentMix(private val failureReason: String, vararg val arguments: Argument) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    var parseMap = ArgumentParseMap(false)

    arguments.forEach {
      with(it.check(guild, arg)) {
        val hasMatch = status
        parseMap = this
        if (hasMatch) return@forEach
      }
    }

    if (!parseMap.status) return ArgumentParseMap(false, failureReason)

    return parseMap
  }
}