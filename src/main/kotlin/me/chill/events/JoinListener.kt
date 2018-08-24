package me.chill.events

import me.chill.database.TargetChannel
import me.chill.database.addServer
import me.chill.database.getChannel
import me.chill.exceptions.TaigaException
import me.chill.utility.embed
import me.chill.utility.send
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class JoinListener : ListenerAdapter() {
	override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during member join")

		val serverId = event.guild.id
		val joinChannelId = getChannel(TargetChannel.Join, serverId)
		val joinChannel = event.jda.getTextChannelById(joinChannelId)

		joinChannel.send(newMemberJoinEmbed(event))
	}

	override fun onGuildJoin(event: GuildJoinEvent?) {
		if (event == null) throw TaigaException("Event object was null during bot join")

		val serverId = event.guild.id
		val defaultChannelId = event.guild.defaultChannel!!.id

		addServer(serverId, defaultChannelId)
		event.guild.getTextChannelById(defaultChannelId).send(botJoinEmbed(event.guild.name))
	}
}

private fun newMemberJoinEmbed(event: GuildMemberJoinEvent) =
	embed {
		setTitle("Member join")
		setColor(16559701)
		addField(
			"Minori senses a disturbance in the force",
			"Minori spots ${event.member.asMention}(${event.member.effectiveName}#${event.member.user.discriminator})",
			false
		)
		addField(
			"Getting started",
			"Read the #rules-and-info",
			false
		)
		setThumbnail(event.member.user.avatarUrl)
	}

private fun botJoinEmbed(serverName: String) =
	embed {
		setTitle("Hello $serverName")
		setColor(5635287)
		addField(
			"Greetings",
			"So happy to be here! I am Taiga, a bot made by <@302385772718325760>",
			false
		)
		addField(
			"Support",
			"My GitHub repository: <https://github.com/woojiahao/Taiga>",
			false
		)
		setThumbnail("https://media.giphy.com/media/SpzdWtMmREEaA/giphy.gif")
	}