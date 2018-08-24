package me.chill.commands.events

import me.chill.database.TargetChannel
import me.chill.database.addServer
import me.chill.database.getChannel
import me.chill.exception.TaigaException
import me.chill.utility.cyan
import me.chill.utility.embed
import me.chill.utility.green
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

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

		addServer(serverId, defaultChannelId)
		event.guild.getTextChannelById(defaultChannelId).send(botJoinEmbed(event.guild.name))
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

private fun botJoinEmbed(serverName: String) =
	embed {
		title = "Hello $serverName"
		color = cyan
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