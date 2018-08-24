package me.chill.commands.events

import me.chill.database.TargetChannel
import me.chill.database.addServer
import me.chill.database.getChannel
import me.chill.exception.TaigaException
import me.chill.utility.embed
import me.chill.utility.green
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.text.SimpleDateFormat
import java.util.*

// todo: allow customization of roles that is assigned on join
class OnJoinEvent : ListenerAdapter() {
	override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during member join")

		val serverId = event.guild.id
		val joinChannelId = getChannel(TargetChannel.Join, serverId)
		val joinChannel = event.jda.getTextChannelById(joinChannelId)
		val member = event.member

		joinChannel.send(newMemberJoinEmbed(member))
	}

	override fun onGuildJoin(event: GuildJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during bot join")

		val serverId = event.guild.id
		val defaultChannelId = event.guild.defaultChannel!!.id
		val dateTime = SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().time)

		println("Joined ${event.guild.name}::$serverId on $dateTime")
		addServer(serverId, defaultChannelId)
	}
}

private fun newMemberJoinEmbed(member: Member) =
	embed {
		title = "Member join"
		color = green
		field {
			title = "Minori senses a disturbance in the force"
			description = "Minori spots ${member.asMention}(${member.effectiveName}#${member.user.discriminator})"
			inline = false
		}
		field {
			title = "Getting started"
			description = "Read the #rules-and-info"
			inline = false
		}
		thumbnail = member.user.avatarUrl
	}