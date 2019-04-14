package me.chill.arguments

import me.chill.framework.Command
import net.dv8tion.jda.api.entities.Guild

fun parseArguments(command: Command, guild: Guild, args: List<String>): ParseMap {
  val expectedArgs = command.argumentTypes
  val parseMap = ParseMap()

  expectedArgs.zip(args).forEach {
    with(it.first.check(guild, it.second)) {
      parseMap.parsedValues += message

      if (this is ErrorParseMap) {
        parseMap.status = false
        parseMap.errMsg = message
        return parseMap
      }
    }
  }

  return parseMap
}
