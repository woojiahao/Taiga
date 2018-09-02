package me.chill.events

import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.exception.TaigaException
import me.chill.roles.assignRole
import me.chill.settings.green
import me.chill.utility.embed
import me.chill.utility.getDateTime
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Guild
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

		if (!getWelcomeDisabled(serverId)) joinChannel.send(newMemberJoinEmbed(server, member))
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

// Read the <#482414770654543872>
fun newMemberJoinEmbed(server: Guild, member: Member) =
	embed {
		title = "Member join"
		color = green
		field {
			title = "Hi ${member.effectiveName}! Welcome to ${server.name}"
			description = getWelcomeMessage(server.id)
			inline = false
		}
		thumbnail = member.user.avatarUrl
	}