package me.chill.commands.arguments

import me.chill.commands.arguments.types.*
import me.chill.commands.framework.Command
import net.dv8tion.jda.core.entities.Guild

@Suppress("UNCHECKED_CAST")
fun parseArguments(command: Command, guild: Guild, args: Array<String>): ParseMap {
	val expectedArgs = command.getArgumentList()

	val checks = mutableListOf<Argument>()
	expectedArgs.forEach {
		checks.add(
			when(it) {
				ArgumentType.Word -> WordArgument()
				ArgumentType.Sentence -> SentenceArgument()
				ArgumentType.Integer -> IntegerArgument()
				ArgumentType.UserId -> UserIdArgument()
				ArgumentType.ChannelId -> ChannelIdArgument()
				ArgumentType.RoleId -> RoleIdArgument()
			}
		)
	}

	val parseMap = ParseMap()

	val zipped = checks.zip(args)
	for (pair in zipped) {
		val result = pair.first.check(guild, pair.second)
		val status = result.status
		val parsedValue = result.parseValue

		parseMap.parsedValues.add(parsedValue)

		if (!status) {
			parseMap.status = status
			parseMap.errMsg = result.errMsg
			return parseMap
		}
	}

	return parseMap
}
