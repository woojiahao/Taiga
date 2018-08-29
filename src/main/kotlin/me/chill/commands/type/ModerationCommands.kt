package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType.Integer
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import net.dv8tion.jda.core.entities.MessageHistory

@CommandCategory
fun moderationCommands() = commands {
	name = "Moderation"
	command("nuke") {
		expects(Integer)
		execute {
			val messageChannel = getChannel()
			val arguments = getArguments()
			val numberToNuke = (arguments[0] as String).toInt()
			val guild = getGuild()

			val messages = MessageHistory(messageChannel)
				.retrievePast(numberToNuke + 1)
				.complete()
			guild.getTextChannelById(messageChannel.id).deleteMessages(messages)
				.queue()
		}
	}
}