package me.chill.events

import me.chill.database.TargetChannel
import me.chill.database.preference.*
import me.chill.exception.TaigaException
import me.chill.roles.assignRole
import me.chill.settings.green
import me.chill.utility.embed
import me.chill.utility.getDateTime
import me.chill.utility.printMember
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

// todo: allow owner to customize what the welcome embed looks like
class OnJoinEvent : ListenerAdapter() {
	override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during member join")

		val server = event.guild
		val serverId = server.id
		val joinChannelId = getChannel(TargetChannel.Join, serverId)
		val joinChannel = event.jda.getTextChannelById(joinChannelId)
		val member = event.member

		if (!getWelcomeDisabled(serverId)) joinChannel.send(newMemberJoinEmbed(member))
		if (hasJoinRole(serverId)) assignRole(server, joinChannel, getJoinRole(serverId)!!, member.user.id, true)
	}

	override fun onGuildJoin(event: GuildJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during bot join")

		val serverId = event.guild.id
		val defaultChannelId = event.guild.defaultChannel!!.id

		println("Joined ${event.guild.name}::$serverId on ${getDateTime()}")
		addServerPreference(serverId, defaultChannelId)
	}
}

private fun newMemberJoinEmbed(member: Member) =
	embed {
		title = "Member join"
		color = green
		field {
			title = "Minori senses a disturbance in the force"
			description = "Minori spots ${printMember(member)}"
			inline = false
		}
		field {
			title = "Getting started"
			description = "Read the <#482414770654543872>"
			inline = false
		}
		thumbnail = member.user.avatarUrl
	}