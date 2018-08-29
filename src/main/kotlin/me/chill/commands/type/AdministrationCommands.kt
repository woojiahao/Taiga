package me.chill.commands.type

import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.database.TargetChannel
import me.chill.database.editChannel
import me.chill.utility.jda.send
import me.chill.utility.jda.successEmbed
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

@CommandCategory
fun administrationCommands() = commands {
	name = "Administration"
	command("setlog") {
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Logging, channel, guild)
		}
	}

	command("setjoin") {
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Join, channel, guild)
		}
	}

	command("setsuggestion") {
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Suggestion, channel, guild)
		}
	}
}

private fun setChannel(targetChannel: TargetChannel, channel: MessageChannel, guild: Guild) {
	val channelId = channel.id
	val serverId = guild.id

	editChannel(targetChannel, serverId, channelId)
	channel.send(
		successEmbed(
			"Channel Assigned",
			"${targetChannel.name} channel has been assigned to #${channel.name} in **${guild.name}**"))
}