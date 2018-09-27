package me.chill.commands

import me.chill.arguments.types.DiscordInvite
import me.chill.arguments.types.Prefix
import me.chill.arguments.types.Word
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.database.states.TimeMultiplier
import me.chill.embed.types.preferenceEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.createRole
import me.chill.roles.getRole
import me.chill.roles.hasRole
import me.chill.settings.orange
import me.chill.settings.serve
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.send
import me.chill.utility.jda.simpleEmbed
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

@CommandCategory
fun administrationCommands() = commands("Administration") {
	command("setlog") {
		execute {
			setChannel(TargetChannel.Logging, channel, guild)
		}
	}

	command("setjoin") {
		execute {
			setChannel(TargetChannel.Join, channel, guild)
		}
	}

	command("setsuggestion") {
		execute {
			setChannel(TargetChannel.Suggestion, channel, guild)
		}
	}

	command("setup") {
		execute {
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

	// todo: introduce regex to check for where the <prefix>help is found?
	command("setprefix") {
		expects(Prefix())
		execute {
			val newPrefix = arguments[0]!!.str()
			val botAsMember = guild.getMember(jda.selfUser)
			val originalNickname = if (botAsMember.nickname != null) {
				botAsMember.nickname.substring(0, botAsMember.nickname.lastIndexOf("("))
			} else {
				botAsMember.effectiveName
			}
			editPrefix(guild.id, newPrefix)
			guild.controller.setNickname(guild.getMember(jda.selfUser), "${originalNickname} (${newPrefix}help)").complete()
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
			respond(
				simpleEmbed(
					"${guild.name} Prefix",
					"Current prefix is: **$serverPrefix**",
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
					"Current time multiplier for **${guild.name}** is in **${getTimeMultiplier(guild.id).fullTerm}s**"
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
			val newTimeMultiplier = arguments[0]!!.str()

			val timeMultiplier = when {
				shortForm.map { short -> short.toLowerCase() }.contains(newTimeMultiplier) ->
					TimeMultiplier.valueOf(newTimeMultiplier.toUpperCase())
				longForm.contains(newTimeMultiplier) ->
					timeMultiplers.filter { multiplier -> multiplier.fullTerm == newTimeMultiplier }[0]
				longFormPlural.contains(newTimeMultiplier) ->
					timeMultiplers.filter { multiplier -> "${multiplier.fullTerm}s" == newTimeMultiplier }[0]
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
			respond(preferenceEmbed(guild, getAllPreferences(guild.id)))
		}
	}

	command("setpreference") {
		val words = arrayOf(
			"tm", "timemultiplier",
			"rml", "messagelimit",
			"rmd", "messageduration",
			"rre", "roleexcluded",
			"jr", "joinrole"
		)

		expects(Word(words), Word())
		execute {
			val preference = arguments[0]!!.str()
			val setting = arguments[1]!!.str()
			when (preference) {
				"tm", "timemultiplier" -> {
				}
				"rml", "messagelimit" -> {
					val limit = setting.toIntOrNull()
					if (limit == null) {
						respond(
							failureEmbed(
								"Invalid Setting",
								"Message limit: **$setting** is invalid, pass an integer the next time"
							)
						)
						return@execute
					}

					if (limit < 1) {
						respond(
							failureEmbed(
								"Invalid setting",
								"Message limit: **$limit** cannot be less than 1"
							)
						)
					}


				}
				"rmd", "messageduration" -> {

				}
				"rre", "roleexcluded" -> {

				}
				"jr", "joinrole" -> {

				}
			}
		}
	}

	command("addinvite") {
		expects(DiscordInvite())
		execute {
			val invite = arguments[0]!!.str()
			addToWhitelist(guild.id, invite)
			respond(
				successEmbed(
					"Invite Added To Whitelist",
					"Invite: $invite is now whitelisted and can be sent by any member",
					null
				)
			)
		}
	}

	command("removeinvite") {
		expects(DiscordInvite(true))
		execute {
			val invite = arguments[0]!!.str()
			removeFromWhitelist(guild.id, invite)
			respond(
				successEmbed(
					"Invite Removed From Whitelist",
					"Invite: $invite has been removed from the whitelist",
					null
				)
			)
		}
	}

	command("whitelist") {
		execute {
			val whitelist = getWhitelist(guild.id)
			respond(
				successEmbed(
					"${guild.name} Whitelist",
					if (whitelist.isBlank()) "No invites whitelisted"
					else whitelist,
					null
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
			"${targetChannel.name} channel has been assigned to #${channel.name} in **${guild.name}**"
		)
	)
}

private fun setupMuted(guild: Guild) {
	val mutedName = "muted"
	if (!guild.hasRole(mutedName)) guild.createRole(mutedName)

	val muted = guild.getRole(mutedName)

	guild.textChannels.forEach {
		val hasOverride = it.rolePermissionOverrides.any { perm ->
			perm.role.name.toLowerCase() == mutedName.toLowerCase()
		}

		if (!hasOverride) {
			it.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE).queue()
		}
	}
}