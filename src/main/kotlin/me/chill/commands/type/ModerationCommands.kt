package me.chill.commands.type

import me.chill.commands.framework.CommandSet
import me.chill.commands.framework.command
import me.chill.commands.framework.commands
import net.dv8tion.jda.core.entities.MessageHistory

@CommandSet
fun moderationCommands() = commands {
	command("nuke") {
		expects(Int)
		execute {
			val messageChannel = getChannel()
			val arguments = getArguments()
			val numberToNuke = Integer.parseInt(arguments[0])
			val guild = getGuild()

			val messages = MessageHistory(messageChannel)
				.retrievePast(numberToNuke + 1)
				.complete()
			guild.getTextChannelById(messageChannel.id).deleteMessages(messages)
				.queue()
		}
	}
}