package me.chill.commands.categories

import me.chill.commands.container.ContainerKeys
import me.chill.commands.container.command
import me.chill.commands.container.commands
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageHistory


fun moderationCommands() = commands {
	command("nuke") {
		expects(Int)
		execute {
			val messageChannel = args[ContainerKeys.Channel] as MessageChannel
			val arguments = args[ContainerKeys.Input] as Array<String>?
			val guild = args[ContainerKeys.Guild] as Guild
			val numberToNuke = Integer.parseInt(arguments!![0])

			val messages = MessageHistory(messageChannel)
				.retrievePast(numberToNuke + 1)
				.complete()
			guild.getTextChannelById(messageChannel.id).deleteMessages(messages)
				.queue()
		}
	}
}