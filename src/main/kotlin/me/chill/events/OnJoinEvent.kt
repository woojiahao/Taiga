package me.chill.events

import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.exception.TaigaException
import me.chill.roles.assignRole
import me.chill.roles.permanentMute
import me.chill.settings.green
import me.chill.utility.*
import me.chill.utility.jda.embed
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
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
		val joinChannel = server.getTextChannelById(joinChannelId)
		val loggingChannel = server.getTextChannelById(getChannel(TargetChannel.Logging, serverId))
		val member = event.member

		if (!getWelcomeDisabled(serverId)) joinChannel.send(newMemberJoinEmbed(server, member))
		if (hasJoinRole(serverId)) assignRole(server, joinChannel, getJoinRole(serverId)!!, member.user.id, true)
		if (hasRaider(serverId, member.user.id)) {
			permanentMute(server, loggingChannel, member.user.id)
			loggingChannel.send(
				failureEmbed(
					"Raider Rejoin",
					"Raider: ${printMember(member)} as rejoined the server"
				)
			)
		}
	}

	override fun onGuildJoin(event: GuildJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during bot join")

		val serverId = event.guild.id
		val defaultChannelId = event.guild.defaultChannel!!.id

		println("Joined ${event.guild.name}::$serverId on ${getDateTime()}")
		addServerPreference(serverId, defaultChannelId)
	}
}

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