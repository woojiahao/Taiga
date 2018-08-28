package me.chill.commands.type

import me.chill.commands.framework.CommandSet
import me.chill.commands.framework.command
import me.chill.commands.framework.commands
import me.chill.database.TargetChannel
import me.chill.database.editChannel
import me.chill.gifs.happy
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.roles.assignRole
import me.chill.utility.roles.removeRole
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.Role

@CommandSet
fun administrationCommands() = commands {
	command("setlog") {
		expects(String)
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Logging, channel, guild)
		}
	}

	command("setjoin") {
		expects(String)
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Join, channel, guild)
		}
	}

	command("setsuggestion") {
		expects(String)
		execute {
			val channel = getChannel()
			val guild = getGuild()

			setChannel(TargetChannel.Suggestion, channel, guild)
		}
	}

	command("roles") {
		execute {
			val channel = getChannel()
			val guild = getGuild()

			val roles = guild.roles
			channel.send(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(String, String)
		execute {
			val channel = getChannel()
			val guild = getGuild()
			val arguments = getArguments()

			assignRole(guild, channel, arguments[0], arguments[1])
		}
	}

	command("unassign") {
		expects(String, String)
		execute {
			val channel = getChannel()
			val guild = getGuild()
			val arguments = getArguments()

			removeRole(guild, channel, arguments[0], arguments[1])
		}
	}
}

private fun listRolesEmbed(guild: Guild, roles: List<Role>) =
	embed {
		title = "Roles in ${guild.name}"
		color = green
		description = roles.joinToString("\n") {
			"${it.name} :: ${it.id}"
		}
		thumbnail = guild.iconUrl
	}

private fun channelSetEmbed(targetChannel: TargetChannel, channelName: String, guildName: String) =
	embed {
		title = "Channel assigned"
		description = "${targetChannel.name} channel has been assigned to #$channelName in **$guildName**"
		color = green
		thumbnail = happy
	}

private fun setChannel(targetChannel: TargetChannel, channel: MessageChannel, guild: Guild) {
	val channelId = channel.id
	val serverId = guild.id

	editChannel(targetChannel, serverId, channelId)
	channel.send(channelSetEmbed(targetChannel, channel.name, guild.name))
}