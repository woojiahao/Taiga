package me.chill.commands.categories

import me.chill.commands.container.command
import me.chill.commands.container.commands
import net.dv8tion.jda.core.entities.MessageHistory


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