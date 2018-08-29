package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType.RoleId
import me.chill.commands.arguments.ArgumentType.UserId
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.database.TargetChannel
import me.chill.database.editChannel
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.roles.assignRole
import me.chill.utility.roles.removeRole
import me.chill.utility.settings.green
import me.chill.utility.settings.happy
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.Role

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

	command("roles") {
		execute {
			val guild = getGuild()

			val roles = guild.roles
			respond(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(RoleId, UserId)
		execute {
			val arguments = getArguments()

			assignRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String)
		}
	}

	command("unassign") {
		expects(RoleId, UserId)
		execute {
			val arguments = getArguments()

			removeRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String)
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