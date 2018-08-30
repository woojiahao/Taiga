package me.chill.commands.type

import me.chill.commands.arguments.types.ChannelId
import me.chill.commands.arguments.types.Integer
import me.chill.commands.arguments.types.Sentence
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.utility.failureEmbed
import me.chill.utility.send
import me.chill.settings.noWay
import net.dv8tion.jda.core.entities.MessageHistory

@CommandCategory
fun moderationCommands() = commands {
	name = "Moderation"
	command("nuke") {
		expects(Integer(0, 99))
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

	command("echo") {
		expects(ChannelId(), Sentence())
		execute {
			val args = getArguments()
			val message = args[1] as String
			val messageChannel = getGuild().getTextChannelById(args[0] as String)
			if (message.contains(Regex("(<@\\d*>)|(<@&\\d*>)|(<@everyone>)|(<@here>)"))) {
				respond(
					failureEmbed(
						"Echo",
						"Cannot echo a message with a member/role mention",
						thumbnail = noWay
					)
				)
				return@execute
			}
			messageChannel.send(args[1] as String)
		}
	}
}