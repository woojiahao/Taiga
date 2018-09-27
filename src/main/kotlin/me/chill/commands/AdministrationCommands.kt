package me.chill.commands

import me.chill.arguments.types.*
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.database.states.TimeMultiplier
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.embed.types.preferenceEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.createRole
import me.chill.roles.getRole
import me.chill.roles.hasRole
import me.chill.settings.clap
import me.chill.utility.jda.cleanEmbed
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.printChannel
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageEmbed
import org.apache.commons.lang3.text.WordUtils

private val preferences = arrayOf(
	"prefix", "multiplier", "logging",
	"join", "suggestion", "messagelimit",
	"messageduration", "raidexcluded",
	"welcomemessage", "joinrole"
)

@CommandCategory
fun administrationCommands() = commands("Administration") {
	// todo: rename to setchannel
	command("set") {
		expects(Word(TargetChannel.getNames()))
		execute {
			val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
			type.edit(guild.id, channel.id)
			respond(
				cleanEmbed(
					"Channel Assigned",
					"${type.name} channel has been assigned to <#${channel.id}> in **${guild.name}**"
				)
			)
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

	command("disable") {
		expects(Word(TargetChannel.getNames()))
		execute {
			val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
			type.disable(guild.id)
			respond(
				successEmbed(
					"${type.name} Disabled",
					"${type.name}s have been disabled for **${guild.name}**",
					clap
				)
			)
		}
	}

	command("enable") {
		expects(Word(TargetChannel.getNames()))
		execute {
			val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
			type.enable(guild.id)
			respond(
				successEmbed(
					"${type.name} Enabled",
					"${type.name}s have been enabled for **${guild.name}**",
					clap
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
			guild.controller.setNickname(guild.getMember(jda.selfUser), "$originalNickname (${newPrefix}help)").complete()
			respond(
				successEmbed(
					"${guild.name} Prefix Changed",
					"Prefix has been changed to **$newPrefix**"
				)
			)
		}
	}
	command("settimemultiplier") {
		expects(Word(TimeMultiplier.getNames()))
		execute {
			val multiplier = arguments[0]!!.str()

			val timeMultiplier = when (multiplier) {
				"m", "s", "h", "d" -> TimeMultiplier.valueOf(multiplier.toUpperCase())
				"minute", "second", "hour", "day" -> TimeMultiplier.values().first { mul -> mul.fullTerm == multiplier }
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

	command("getpreference") {
		expects(Word(preferences))
		execute {
			respond(displayPreference(arguments[0]!!.str(), guild, invoker))
		}
	}

	command("setpreference") {
		expects(Word(preferences), Sentence())
		execute {
			respond(setPreference(arguments[0]!!.str(), arguments[1]!!.str(), guild, invoker, jda))
		}
	}
}

private fun setPreference(preference: String, input: String, guild: Guild, invoker: Member, jda: JDA): MessageEmbed? {
	return when (preference) {
		"prefix" -> {
			val parseMap = Prefix().check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to change prefix", parseMap.errMsg)
			}

			setPrefix(input, guild.getMember(jda.selfUser), guild)
			cleanEmbed("${guild.name} Prefix Changed", "Prefix has been changed to **$input**")
		}
		"multiplier" -> {
			val parseMap = Word(TimeMultiplier.getNames()).check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to change multiplier", parseMap.errMsg)
			}

			setMultiplier(input, guild.id)
			cleanEmbed("${guild.name} Multiplier Changed", "Multiplier has been changed to **$input**")
		}
		"logging", "join", "suggestion" -> {
			val type = TargetChannel.valueOf(WordUtils.capitalize(preference))
			val parseMap = ChannelId().check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to set channel", parseMap.errMsg)
			}

			type.edit(guild.id, parseMap.parsedValue)
			cleanEmbed(
				"Channel Assigned",
				"${type.name} channel has been assigned to <#${parseMap.parsedValue}> in **${guild.name}**"
			)
		}

		"messagelimit" -> {
			val parseMap = Integer(1).check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to set raid message limit", parseMap.errMsg)
			}

			editRaidMessageLimit(guild.id, input.toInt())
			cleanEmbed(
				"Raid Message Limit",
				"Raid message limit for **${guild.name}** has been set to **${input.toInt()}** messages"
			)
		}

		"messageduration" -> {
			val parseMap = Integer(1).check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to set raid message duration", parseMap.errMsg)
			}

			editRaidMessageDuration(guild.id, input.toInt())
			cleanEmbed(
				"Raid Message Duration",
				"Raid message duration for **${guild.name}** has been set to **${input.toInt()}** seconds"
			)
		}
		"raidexcluded" -> {
			val parseMap = RoleId().check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to set raid role excluded", parseMap.errMsg)
			}

			editRaidRoleExcluded(guild.id, parseMap.parsedValue)
			cleanEmbed(
				"Raid Role Excluded",
				"**${guild.getRoleById(parseMap.parsedValue).name} and higher** will be excluded from the raid filter"
			)
		}

		"welcomemessage" -> {
			editWelcomeMessage(guild.id, input)
			newMemberJoinEmbed(guild, invoker)
		}

		"joinrole" -> {
			val parseMap = RoleId().check(guild, input)
			if (!parseMap.status) {
				return failureEmbed("Unable to set member on join role", parseMap.errMsg)
			}

			val roleId = parseMap.parsedValue
			if (roleId == guild.getRolesByName("@everyone", false)[0].id) {
				return failureEmbed(
					"Unable to set member on join role",
					"You cannot assign that role to members on join!"
				)
			}

			editJoinRole(guild.id, roleId)
			cleanEmbed(
				"Member On Join",
				"New members will be assigned **${guild.getRoleById(roleId).name}** on join"
			)
		}

		else -> null
	}

}

private fun setChannel() {

}

private fun setMultiplier(multiplier: String, guildId: String) {
	val timeMultiplier = when (multiplier) {
		"m", "s", "h", "d" -> TimeMultiplier.valueOf(multiplier.toUpperCase())
		"minute", "second", "hour", "day" -> TimeMultiplier.values().first { mul -> mul.fullTerm == multiplier }
		else -> TimeMultiplier.M
	}

	editTimeMultiplier(guildId, timeMultiplier)
}

private fun setPrefix(newPrefix: String, botAsMember: Member, guild: Guild) {
	val originalNickname = if (botAsMember.nickname != null) {
		botAsMember.nickname.substring(0, botAsMember.nickname.lastIndexOf("("))
	} else {
		botAsMember.effectiveName
	}
	editPrefix(guild.id, newPrefix)
	guild.controller.setNickname(guild.getMemberById(botAsMember.user.id), "$originalNickname (${newPrefix}help)").complete()
}

private fun displayPreference(preference: String, guild: Guild, invoker: Member): MessageEmbed? {
	val id = guild.id
	val name = guild.name
	return when (preference) {
		"prefix" -> cleanEmbed("$name Prefix", "Current prefix is: **${getPrefix(id)}**")
		"multiplier" ->
			cleanEmbed(
				"Time Multiplier",
				"Current time multiplier for **$name** is in **${getTimeMultiplier(id).fullTerm}s**")
		"logging", "join", "suggestion" -> {
			val targetChannel = TargetChannel.valueOf(WordUtils.capitalize(preference))
			val targetName = targetChannel.name

			return cleanEmbed(
				"$targetName Channel",
				"Current ${targetName.toLowerCase()} channel is ${printChannel(guild.getTextChannelById(targetChannel.get(guild.id)))}\n" +
					"${targetName}s are currently **${targetChannel.disableStatus(guild.id)}**"
			)!!
		}

		"messagelimit" -> cleanEmbed(
			"Raid Message Limit",
			"The raid message limit for **$name** is **${getRaidMessageLimit(id)}** messages")

		"messageduration" -> cleanEmbed(
			"Raid Message Duration",
			"The raid message duration for **$name** is **${getRaidMessageDuration(id)}** seconds")

		"raidexcluded" -> {
			val raidRoleExcluded = getRaidRoleExcluded(guild.id)
			cleanEmbed(
				"Raid Role Excluded",
				if (raidRoleExcluded == null) "No role is being filtered"
				else "**${guild.getRoleById(raidRoleExcluded).name} and higher** are excluded from the raid filter")
		}

		"welcomemessage" -> newMemberJoinEmbed(guild, invoker)

		"joinrole" -> {
			val message =
				if (!hasJoinRole(guild.id)) {
					"**${guild.name}** currently does not have an auto-assigned role for new members"
				} else {
					val roleId = getJoinRole(guild.id)
					"New members will be assigned **${guild.getRoleById(roleId).name}** on join"
				}
			cleanEmbed("Member On Join", message)
		}

		else -> null
	}
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