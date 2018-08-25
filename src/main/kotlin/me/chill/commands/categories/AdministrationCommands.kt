package me.chill.commands.categories

import me.chill.commands.container.ContainerKeys.*
import me.chill.commands.container.command
import me.chill.commands.container.commands
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

fun administrationCommands() = commands {
	command("setlog") {
		expects(String)
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild

			setChannel(TargetChannel.Logging, channel, guild)
		}
	}

	command("setjoin") {
		expects(String)
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild

			setChannel(TargetChannel.Join, channel, guild)
		}
	}

	command("setsuggestion") {
		expects(String)
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild

			setChannel(TargetChannel.Suggestion, channel, guild)
		}
	}

	command("roles") {
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild

			val roles = guild.roles
			channel.send(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(String, String)
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild
			val arguments = args[Input] as Array<String>

			assignRole(guild, channel, arguments[0], arguments[1])
		}
	}

	command("unassign") {
		expects(String, String)
		execute {
			val channel = args[Channel] as MessageChannel
			val guild = args[Guild] as Guild
			val arguments = args[Input] as Array<String>

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