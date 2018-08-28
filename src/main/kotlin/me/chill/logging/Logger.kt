package me.chill.logging

import me.chill.commands.framework.Command
import me.chill.database.TargetChannel
import me.chill.database.getChannel
import me.chill.utility.blue
import me.chill.utility.general.getDateTime
import me.chill.utility.jda.embed
import me.chill.utility.jda.printChannel
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
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