package me.chill.logging

import me.chill.database.operations.getChannel
import me.chill.database.states.TargetChannel
import me.chill.framework.Command
import me.chill.settings.blue
import me.chill.utility.getDateTime
import me.chill.utility.jda.embed
import me.chill.utility.jda.printChannel
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

fun normalLog(command: Command) {
	val guild = command.guild
	val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guild.id))
	loggingChannel.send(
		normalLogEmbed(
			command.name,
			command.invoker,
			command.channel
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