package me.chill.commands.categories

import me.chill.commands.container.ContainerKeys
import me.chill.commands.container.command
import me.chill.commands.container.commands
import me.chill.database.TargetChannel
import me.chill.database.editChannel
import me.chill.gifs.happy
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

fun administrationCommands() = commands {
	command("setlog") {
		expects(String)
		behavior {
			val channel = args[ContainerKeys.Channel] as MessageChannel
			val guild = args[ContainerKeys.Guild] as Guild

			setChannel(TargetChannel.Logging, channel, guild)
		}
	}

	command("setjoin") {
		expects(String)
		behavior {
			val channel = args[ContainerKeys.Channel] as MessageChannel
			val guild = args[ContainerKeys.Guild] as Guild

			setChannel(TargetChannel.Join, channel, guild)
		}
	}

	command("setsuggestion") {
		expects(String)
		behavior {
			val channel = args[ContainerKeys.Channel] as MessageChannel
			val guild = args[ContainerKeys.Guild] as Guild

			setChannel(TargetChannel.Suggestion, channel, guild)
		}
	}
}

private fun channelSetEmbed(targetChannel: TargetChannel, channelName: String, guildName: String) =
	embed {
		title = "Channel assigned"
		description = "${targetChannel.name} channel has been assigned to #${channelName} in **${guildName}**"
		color = green
		thumbnail = happy
	}

private fun setChannel(targetChannel: TargetChannel, channel: MessageChannel, guild: Guild) {
	val channelId = channel.id
	val serverId = guild.id

	editChannel(targetChannel, serverId, channelId)
	channel.send(channelSetEmbed(targetChannel, channel.name, guild.name))
}