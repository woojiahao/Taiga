package me.chill.logging

import me.chill.database.states.TargetChannel
import me.chill.framework.Command
import me.chill.settings.blue
import me.chill.settings.pink
import me.chill.utility.getDateTime
import me.chill.utility.embed
import me.chill.utility.printChannel
import me.chill.utility.printMember
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

fun normalLog(command: Command) {
	val guild = command.guild
	guild.getTextChannelById(TargetChannel.Logging.get(guild.id)).send(
		normalLogEmbed(command.name, command.invoker, command.channel)
	)
}

fun macroLog(macroName: String, invoker: Member, channel: MessageChannel, guild: Guild) {
	guild.getTextChannelById(TargetChannel.Logging.get(guild.id)).send(macroLogEmbed(macroName, invoker, channel))
}

private fun macroLogEmbed(macroName: String, invoker: Member, channel: MessageChannel) =
	embed {
		title = "Macro Used"
		color = pink
		description = "**$macroName** was used by ${printMember(invoker)} in ${printChannel(channel)}"
		footer {
			message = getDateTime()
			iconUrl = invoker.user.avatarUrl
		}
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