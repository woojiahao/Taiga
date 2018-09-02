package me.chill.logging

import me.chill.framework.Command
import me.chill.database.states.TargetChannel
import me.chill.database.operations.getChannel
import me.chill.settings.blue
import me.chill.utility.getDateTime
import me.chill.utility.embed
import me.chill.utility.printChannel
import me.chill.utility.printMember
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

fun normalLog(command: Command) {
	val guild = command.getGuild()
	val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guild.id))
	loggingChannel.send(
		normalLogEmbed(
			command.name,
			command.getInvoker(),
			command.getChannel()
		)
	)
}

private fun normalLogEmbed(commandName: String, invoker: Member, channel: MessageChannel) =
	embed {
		title = "Command Invoked"
		color = blue
		description = "**$commandName** invoked by ${printMember(invoker)} in ${printChannel(channel)}"
		footer {
			message = getDateTime()
			iconUrl = invoker.user.avatarUrl
		}
	}