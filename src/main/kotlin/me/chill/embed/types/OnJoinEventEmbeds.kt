package me.chill.embed.types

import me.chill.database.operations.getWelcomeMessage
import me.chill.settings.green
import me.chill.utility.jda.embed
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member

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