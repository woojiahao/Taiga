package me.chill.commands

import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.database.TargetChannel
import me.chill.database.editChannel
import me.chill.roles.createRole
import me.chill.utility.getRole
import me.chill.utility.hasRole
import me.chill.utility.send
import me.chill.utility.successEmbed
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

// todo: allow users to customize how they want the default time element to be like
@CommandCategory
fun administrationCommands() = commands("Administration") {
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

	command("setup") {
		execute {
			val guild = getGuild()
			setupMuted(guild)
			respond(
				successEmbed(
					"Set-Up Completed",
					"Bot has been set up for **${guild.name}**\n" +
						"Remember to move the `muted` role higher in order for it to take effect"
				)
			)
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

private fun setupMuted(guild: Guild) {
	val mutedName = "muted"
	if (!guild.hasRole(mutedName)) createRole(guild, mutedName)

	val muted = guild.getRole(mutedName)

	guild.textChannels.forEach { channel ->
		val hasOverride = channel.rolePermissionOverrides.any {
			it.role.name.toLowerCase() == mutedName.toLowerCase()
		}

		if (!hasOverride) channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE).queue()
	}
}