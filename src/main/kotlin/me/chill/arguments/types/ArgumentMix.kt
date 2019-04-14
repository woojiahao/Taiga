package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.arguments.ErrorParseMap
import me.chill.arguments.SuccessParseMap
import net.dv8tion.jda.api.entities.Guild

/**
 * Represents multi-args for the same position in the argument list
 * All arguments passed must be unique otherwise, an exception will be thrown
 */
// TODO: Test this to make sure the logic is right
class ArgumentMix(private val failureReason: String, vararg val arguments: Argument) : Argument {
  override fun check(guild: Guild, arg: String): ArgumentParseMap {
    val checkedArguments = arguments.map { it.check(guild, arg) }
    val hasMatchCondition: (ArgumentParseMap) -> Boolean = { it is SuccessParseMap }
    if (checkedArguments.none(hasMatchCondition)) return ErrorParseMap(failureReason)

    return checkedArguments.first(hasMatchCondition)
  }
}