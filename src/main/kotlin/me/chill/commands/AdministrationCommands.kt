package me.chill.commands

import me.chill.arguments.types.Prefix
import me.chill.arguments.types.Word
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.database.states.TimeMultiplier
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.createRole
import me.chill.settings.orange
import me.chill.settings.serve
import me.chill.utility.*
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

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

	command("setprefix") {
		expects(Prefix())
		execute {
			val guild = getGuild()
			val newPrefix = getArguments()[0] as String
			editPrefix(guild.id, newPrefix)
			respond(
				successEmbed(
					"${guild.name} Prefix Changed",
					"Prefix has been changed to **$newPrefix**"
				)
			)
		}
	}

	command("getprefix") {
		execute {
			val guild = getGuild()
			respond(
				simpleEmbed(
					"${guild.name} Prefix",
					"Current prefix is: **${getServerPrefix()}**",
					serve,
					orange
				)
			)
		}
	}

	command("gettimemultiplier") {
		execute {
			respond(
				successEmbed(
					"Time Multiplier",
					"Current time multiplier for **${getGuild().name}** is in **${getTimeMultiplier(getGuild().id).fullTerm}s**"
				)
			)
		}
	}

	command("settimemultiplier") {
		val timeMultiplers = TimeMultiplier.values()
		val inclusion = mutableListOf<String>()
		val shortForm = timeMultiplers.map { it.name }
		val longForm = timeMultiplers.map { it.fullTerm }
		val longFormPlural = timeMultiplers.map { "${it.fullTerm}s" }
		inclusion.addAll(shortForm)
		inclusion.addAll(longForm)
		inclusion.addAll(longFormPlural)
		expects(Word(inclusion = inclusion.toTypedArray()))
		execute {
			val guild = getGuild()
			val newTimeMultipler = getArguments()[0] as String

			val timeMultiplier = when {
				shortForm.map { short -> short.toLowerCase() }.contains(newTimeMultipler) ->
					TimeMultiplier.valueOf(newTimeMultipler.toUpperCase())
				longForm.contains(newTimeMultipler) ->
					timeMultiplers.filter { multiplier -> multiplier.fullTerm == newTimeMultipler }[0]
				longFormPlural.contains(newTimeMultipler) ->
					timeMultiplers.filter { multiplier -> "${multiplier.fullTerm}s" == newTimeMultipler }[0]
				else -> TimeMultiplier.M
			}

			editTimeMultiplier(guild.id, timeMultiplier)
			respond(
				successEmbed(
					"Time Multiplier",
					"Time multiplier for **${guild.name}** has been set to **${timeMultiplier.fullTerm}s**"
				)
			)
		}
	}

	command("getpreferences") {
		execute {
			val guild = getGuild()
			val allPreferences = getAllPreferences(guild.id)
			respond(
				successEmbed(
					"${guild.name} Preferences",
					allPreferences,
					thumbnail = null
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