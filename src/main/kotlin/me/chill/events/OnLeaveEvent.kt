package me.chill.events

import me.chill.database.operations.TargetChannel
import me.chill.database.operations.clearPermissions
import me.chill.database.operations.removeServerPreference
import me.chill.exception.ListenerEventException
import me.chill.settings.lost
import me.chill.settings.red
import me.chill.utility.getDateTime
import me.chill.utility.jda.embed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import me.chill.utility.jda.successEmbed
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnLeaveEvent : ListenerAdapter() {
	override fun onGuildMemberLeave(event: GuildMemberLeaveEvent?) {
		event ?: throw ListenerEventException(
			"On Member Leave",
			"Event object was null during member leave"
		)

		val serverId = event.guild.id
		val member = event.member

		val loggingChannel = event.guild.getTextChannelById(TargetChannel.Logging.get(serverId))
		loggingChannel.send(memberLeaveEmbed(member))
	}

	override fun onGuildLeave(event: GuildLeaveEvent?) {
		event ?: throw ListenerEventException(
			"On Bot Leave",
			"Event object was null during bot leave"
		)

		val serverId = event.guild.id

		event.guild.jda.getTextChannelById("482338281946742786").send(
			successEmbed(
				"Server Leave",
				"Left ${event.guild.name}::$serverId on ${getDateTime()}"
			)
		)

		removeServerPreference(serverId)
		clearPermissions(serverId)
	}
}

private fun memberLeaveEmbed(member: Member) =
	embed {
		title = "Member leave"
		color = red
		description = "${printMember(member)} left the server"
		thumbnail = lost
	}