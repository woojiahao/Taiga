package me.chill.commands

import me.chill.arguments.types.ChannelId
import me.chill.arguments.types.Integer
import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.database.states.TimeMultiplier
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.infraction.UserInfractionRecord
import me.chill.roles.assignRole
import me.chill.roles.removeRole
import me.chill.settings.*
import me.chill.utility.*
import me.chill.utility.jda.*
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timerTask

@CommandCategory
fun moderationCommands() = commands("Moderation") {
	command("nuke") {
		expects(Integer(0, 99))
		execute {
			val messageChannel = getChannel()

			getGuild().deleteMessagesFromChannel(
				messageChannel.id,
				messageChannel.getMessageHistory(getArguments()[0]!!.int() + 1)
			)
		}
	}

	command("echo") {
		expects(ChannelId(), Sentence())
		execute {
			val args = getArguments()
			val message = args[1] as String
			val messageChannel = getGuild().getTextChannelById(args[0] as String)
			if (message.contains(Regex("(<@\\d*>)|(<@&\\d*>)|(@everyone)|(@here)"))) {
				respond(
					failureEmbed(
						"Echo",
						"Cannot echo a message with a member/role mention",
						thumbnail = noWay
					)
				)
				return@execute
			}
			messageChannel.send(args[1] as String)
		}
	}

	command("mute") {
		expects(UserId(), Integer(), Sentence())
		execute {
			val args = getArguments()
			val guild = getGuild()

			val targetId = args[0] as String
			val target = guild.getMemberById(targetId)

			val duration = args[1]!!.int()
			val reason = args[2] as String

			muteUser(guild, getChannel(), target, duration, getServerPrefix(), reason)
		}
	}

	command("history") {
		expects(UserId(true))
		execute {
			val targetId = getArguments()[0] as String
			respond(historyEmbed(getGuild(), getJDA().findUser(targetId), getJDA(), getHistory(getGuild().id, targetId)))
		}
	}

	command("strike") {
		expects(UserId(), Integer(0, 3), Sentence())
		execute {
			val args = getArguments()
			val targetId = args[0] as String
			val strikeWeight = args[1]!!.int()
			val strikeReason = args[2] as String

			strikeUser(getGuild(), targetId, getChannel(), strikeWeight, strikeReason, getInvoker())
		}
	}

	command("warn") {
		expects(UserId(), Sentence())
		execute {
			val targetId = getArguments()[0] as String
			val strikeReason = getArguments()[1] as String
			strikeUser(getGuild(), targetId, getChannel(), 0, strikeReason, getInvoker())
		}
	}

	command("wiperecord") {
		expects(UserId(true))
		execute {
			val guild = getGuild()
			val targetId = getArguments()[0] as String
			wipeRecord(guild.id, targetId)
			respond(
				successEmbed(
					"Records Wiped",
					"User: **${getJDA().findUser(targetId).name}**'s history has been wiped!",
					clap
				)
			)
		}
	}
}

private fun historyEmbed(guild: Guild, user: User, jda: JDA, userInfractionRecord: UserInfractionRecord) =
	embed {
		val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
		val joinDate = if (guild.getMember(user) == null) {
			"User is not on the server"
		} else {
			guild.getMember(user).joinDate.format(dateTimeFormatter)
		}

		title = "${user.name}'s History"
		color = cyan
		thumbnail = user.avatarUrl

		field {
			title = "Summary"
			description =
				"${user.name}#${user.discriminator} has **${userInfractionRecord.getStrikes().size}** infraction(s)\n" +
				"Current Strike Count: **${getStrikeCount(guild.id, user.id)}/3**\n" +
				"Join Date: **$joinDate**\n" +
				"Creation Date: **${user.creationTime.format(dateTimeFormatter)}**"
		}

		field {}

		if (userInfractionRecord.getStrikes().isEmpty()) {
			field {
				title = "This user has no infractions"
				description = "Squeaky clean!"
			}
		} else {
			userInfractionRecord.getStrikes().forEach { userStrike ->
				val isExpired = if (DateTime.now() > userStrike.expiryDate) "expired" else "not expired"

				field {
					title = "ID :: ${userStrike.strikeId} :: Weight :: ${userStrike.strikeWeight}"
					description =
						"This infraction is **$isExpired**\n" +
						"Issued by **${jda.findUser(userStrike.actingModeratorId).name}** " +
						"on **${DateTimeFormat.forPattern("dd-MM-yyyy").print(userStrike.strikeDate)}**\n" +
						"__**Reason:**__\n${userStrike.strikeReason}"
				}
			}
		}
	}

private fun strikeSuccessEmbed(strikeWeight: Int, target: Member, strikeReason: String) =
	embed {
		title = "User Striked"
		color = orange
		description = "${printMember(target)} has been striked"

		field {
			title = "Reason"
			description = strikeReason
			inline = false
		}

		field {
			title = "Weight"
			description = strikeWeight.toString()
			inline = false
		}
	}

private fun userStrikeNotificationEmbed(guildName: String, strikeReason: String,
										strikeWeight: Int, strikeCount: Int) =
	embed {
		title = "Strike"
		description = "You have been striked in **$guildName**"
		color = red

		field {
			title = "Reason"
			description = strikeReason
		}

		field {
			title = "Weight"
			description = strikeWeight.toString()
		}

		field {
			title = "Infraction Status"
			description = "Your strike count is at **$strikeCount/3**"
		}
	}

private fun strikeUser(guild: Guild, targetId: String, channel: MessageChannel,
					   strikeWeight: Int, strikeReason: String, invoker: Member) {
	val guildId = guild.id
	val target = guild.getMemberById(targetId)
	val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guildId))

	addStrike(guildId, targetId, strikeWeight, strikeReason, invoker.user.id)
	val strikeCount = getStrikeCount(guildId, targetId)
	guild
		.getMemberById(targetId)
		.sendPrivateMessage(
			userStrikeNotificationEmbed(guild.name, strikeReason, strikeWeight, strikeCount)
		)

	loggingChannel.send(
		strikeSuccessEmbed(strikeWeight, target, strikeReason)
	)

	when {
		strikeCount == 1 -> muteUser(
			guild,
			channel,
			target,
			1,
			getPrefix(guildId),
			"Muted due to infraction",
			TimeMultiplier.H
		)
		strikeCount == 2 -> muteUser(
			guild,
			channel,
			target,
			1,
			getPrefix(guildId),
			"Muted due to infraction",
			TimeMultiplier.D
		)
		strikeCount >= 3 -> guild.controller.ban(target, 1, strikeReason).complete()
	}
}

private fun muteUser(guild: Guild, channel: MessageChannel,
					 target: Member, duration: Int,
					 serverPrefix: String, reason: String,
					 timeMultiplier: TimeMultiplier? = null) {
	val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guild.id))
	val targetId = target.user.id

	if (!guild.hasRole("muted")) {
		channel.send(
			failureEmbed(
				"Mute Failed",
				"Unable to apply mute to user as the **muted** role does not exist, run `${serverPrefix}setup`"
			)
		)
		return
	}

	val guildTimeMultiplier = getTimeMultiplier(guild.id)

	val mutedRole = guild.getRole("muted")
	assignRole(guild, channel, mutedRole.id, targetId)
	target.sendPrivateMessage(userMuteNotificationEmbed(guild.name, duration, reason, guildTimeMultiplier))

	val muteDuration = if (timeMultiplier != null) {
		duration * timeMultiplier.multiplier
	} else {
		duration * guildTimeMultiplier.multiplier
	}

	Timer().schedule(
		timerTask {
			removeRole(guild, channel, mutedRole.id, targetId)
			target.sendPrivateMessage(
				simpleEmbed(
					"Unmuted",
					"You have been unmuted in **${guild.name}**",
					null,
					cyan
				)
			)

			loggingChannel.send(
				simpleEmbed(
					"User Unmuted",
					"User: ${printMember(target)} has been unmuted",
					thumbnail = null,
					color = orange
				)
			)
		},
		muteDuration
	)

	loggingChannel.send(muteSuccessEmbed(target, duration, reason, guildTimeMultiplier))
}

private fun userMuteNotificationEmbed(guildName: String, duration: Int, reason: String, guildTimeMultiplier: TimeMultiplier) =
	embed {
		title = "Mute"
		description = "You have been muted in **$guildName**"
		color = red

		field {
			title = "Reason"
			description = reason
		}

		field {
			title = "Duration"
			description = "$duration ${guildTimeMultiplier.fullTerm}(s)"
		}
	}

private fun muteSuccessEmbed(member: Member, duration: Int, reason: String, guildTimeMultiplier: TimeMultiplier) =
	embed {
		title = "User Muted"
		description = "User: ${printMember(member)} has been muted for **$duration** ${guildTimeMultiplier.fullTerm}(s)"
		color = green
		field {
			title = "Reason"
			description = reason
		}
	}