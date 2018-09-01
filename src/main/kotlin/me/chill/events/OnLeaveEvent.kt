package me.chill.events

import me.chill.database.states.TargetChannel
import me.chill.database.getChannel
import me.chill.database.removeServerPreference
import me.chill.exception.TaigaException
import me.chill.settings.lost
import me.chill.settings.red
import me.chill.utility.embed
import me.chill.utility.getDateTime
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnLeaveEvent : ListenerAdapter() {
	override fun onGuildMemberLeave(event: GuildMemberLeaveEvent?) {
		if (event == null) throw TaigaException("Event object was null during member leave")

		val serverId = event.guild.id
		val member = event.member

		val loggingChannel = event.guild.getTextChannelById(getChannel(TargetChannel.Logging, serverId))
		loggingChannel.send(memberLeaveEmbed(member))
	}

	override fun onGuildLeave(event: GuildLeaveEvent?) {
		if (event == null) throw TaigaException("Event object was null during bot leave")

		val serverId = event.guild.id

		println("Left ${event.guild.name}::$serverId on ${getDateTime()}")
		removeServerPreference(serverId)
	}
}

private fun memberLeaveEmbed(member: Member) =
	embed {
		title = "Member leave"
		color = red
		description = "${member.effectiveName}#${member.user.discriminator}::${member.asMention} left the server"
		thumbnail = lost
	}