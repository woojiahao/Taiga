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
    val args = arg.split(pipe)

    if (limit != -1 && args.size > limit) {
      return ArgumentParseMap(false, "Cannot have more than **$limit** element(s) in the argument list")
    }

    val individualChecks = args.map { argumentType.check(guild, it) }

    val failingParse: (ArgumentParseMap) -> Boolean = { !it.status }
    val hasFailingParse = individualChecks.any(failingParse)
    if (hasFailingParse) return individualChecks.first(failingParse)

    val parsedValues = individualChecks.map { it.parsedValue }

    return ArgumentParseMap(true, parsedValue = parsedValues.joinToString(pipe.toString()))
  }
}