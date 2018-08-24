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

//		addServer(serverId, defaultChannelId)
		event.guild.getTextChannelById(defaultChannelId).send(botJoinEmbed(event.guild.name))
	}
}

private fun newMemberJoinEmbed(event: GuildMemberJoinEvent) =
	embed {
		title = "Member join"
		color = 16559701
		field {
			title = "Minori senses a disturbance in the force"
			description = "Minori spots ${event.member.asMention}(${event.member.effectiveName}#${event.member.user.discriminator})"
			inline = false
		}
		field {
			title = "Getting started"
			description = "Read the #rules-and-info"
			inline = false
		}
		thumbnail = event.member.user.avatarUrl
	}

private fun botJoinEmbed(serverName: String) =
	embed {
		title = "Hello $serverName"
		color = 5635287
		field {
			title = "Greetings"
			description = "So happy to be here! I am Taiga, a bot made by <@302385772718325760>"
			inline = false
		}
		field {
			title = "Support"
			description =
				"- [Development server](https://discord.gg/xtDNfyw)\n" +
				"- [GitHub repository](https://github.com/woojiahao/Taiga)\n" +
				"- [Invite link](https://discordapp.com/oauth2/authorize?client_id=482340927709511682&scope=bot&permissions=8)"
		}
		thumbnail = "https://media.giphy.com/media/SpzdWtMmREEaA/giphy.gif"
	}