package me.chill.commands.arguments

import me.chill.commands.arguments.types.*
import me.chill.commands.framework.Command
import net.dv8tion.jda.core.entities.Guild

fun parseArguments(command: Command, guild: Guild, args: Array<String>): Boolean {
	val expectedArgs = command.getArgumentList()
	if (expectedArgs.isEmpty()) return true

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
				ArgumentType.CommandName -> CommandNameArgument()
			}
		)
	}

	return checks
		.zip(args)
		.none { it.first.check(guild, it.second) }
}
