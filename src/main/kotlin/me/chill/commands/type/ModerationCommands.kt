package me.chill.commands.type

import me.chill.arguments.types.ChannelId
import me.chill.arguments.types.Integer
import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.credentials
import me.chill.database.TargetChannel
import me.chill.roles.assignRole
import me.chill.roles.removeRole
import me.chill.settings.*
import me.chill.utility.*
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageHistory
import java.util.*
import kotlin.concurrent.timerTask

@CommandCategory
fun moderationCommands() = commands("Moderation") {
	command("nuke") {
		expects(Integer(0, 99))
		execute {
			val messageChannel = getChannel()
			val arguments = getArguments()
			val numberToNuke = (arguments[0] as String).toInt()
			val guild = getGuild()

			val messages = MessageHistory(messageChannel)
				.retrievePast(numberToNuke + 1)
				.complete()
			guild.getTextChannelById(messageChannel.id).deleteMessages(messages)
				.queue()
		}
	}

	command("echo") {
		expects(ChannelId(), Sentence())
		execute {
			val args = getArguments()
			val message = args[1] as String
			val messageChannel = getGuild().getTextChannelById(args[0] as String)
			if (message.contains(Regex("(<@\\d*>)|(<@&\\d*>)|(<@everyone>)|(<@here>)"))) {
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
			val guildName = guild.name

			val targetId = args[0] as String
			val target = guild.getMemberById(targetId)

			val duration = (args[1] as String).toInt()
			val reason = args[2] as String

			val loggingChannel = guild.getTextChannelById(me.chill.database.getChannel(TargetChannel.Logging, guild.id))

			if (!guild.hasRole("muted")) {
				respond(
					failureEmbed(
						"Mute Failed",
						"Unable to apply mute to user as the **muted** role does not exist, run `${credentials!!.prefix}setup`"
					)
				)
				return@execute
			}

			val mutedRole = guild.getRole("muted")
			assignRole(guild, getChannel(), mutedRole.id, targetId, true)
			target.sendPrivateMessage(userMuteNotificationEmbed(guildName, duration, reason))

			Timer().schedule(
				timerTask {
					removeRole(guild, getChannel(), mutedRole.id, targetId, true)
					target.sendPrivateMessage(
						simpleEmbed(
							"Unmuted",
							"You have been unmuted in **$guildName**",
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
				(duration * 60000).toLong()
			)

			loggingChannel.send(muteSuccessEmbed(target, duration, reason))
		}
	}
}

private fun userMuteNotificationEmbed(guildName: String, duration: Int, reason: String) =
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
			description = "$duration minutes"
		}
	}

private fun muteSuccessEmbed(member: Member, duration: Int, reason: String) =
	embed {
		title = "User Muted"
		description = "User: ${printMember(member)} has been muted for **$duration** minutes"
		color = green
		field {
			title = "Reason"
			description = reason
		}
	}