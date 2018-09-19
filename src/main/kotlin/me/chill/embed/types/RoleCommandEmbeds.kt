package me.chill.embed.types

import me.chill.settings.green
import me.chill.utility.jda.embed
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role

fun listRolesEmbed(guild: Guild, roles: List<Role>) =
	embed {
		title = "Roles in ${guild.name}"
		color = green
		description = roles.joinToString("\n") {
			"**${it.name}** :: ${it.id}"
		}
		thumbnail = guild.iconUrl
	}
