package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.api.entities.Guild

/**
 * Represents a list of the same argument type
 */
class ArgumentList(
  private val argumentType: Argument,
  private val pipe: Char = ',',
  private val limit: Int = -1
) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val parsedValues = mutableListOf<String>()

    val args = arg.split(pipe)

    if (limit != -1 && args.size > limit) {
      return ArgumentParseMap(false, "Cannot have more than **$limit** element(s) in the argument list")
    }

    args.forEach {
      val individualCheckMap = argumentType.check(guild, it)
      if (!individualCheckMap.status) return individualCheckMap
      parsedValues.add(individualCheckMap.parsedValue)
    }
    
    return ArgumentParseMap(true, parsedValue = parsedValues.joinToString(pipe.toString()))
  }
}