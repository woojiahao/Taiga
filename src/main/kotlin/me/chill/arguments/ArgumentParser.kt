package me.chill.arguments

import me.chill.framework.Command
import net.dv8tion.jda.api.entities.Guild

fun parseArguments(command: Command, guild: Guild, args: List<String>): ParseMap {
  val expectedArgs = command.argumentTypes
  val parseMap = ParseMap()

  for (pair in expectedArgs.zip(args)) {
    val result = pair.first.check(guild, pair.second)
    val status = result.status
    val parsedValue = result.parsedValue

    parseMap.parsedValues.add(parsedValue)

    if (!status) {
      parseMap.status = status
      parseMap.errMsg = result.errMsg
      return parseMap
    }
  }

  return parseMap
}
