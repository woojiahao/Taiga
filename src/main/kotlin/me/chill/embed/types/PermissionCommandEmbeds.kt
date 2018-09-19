package me.chill.embed.types

import me.chill.database.operations.getPermission
import me.chill.database.operations.hasPermission
import me.chill.framework.CommandContainer
import me.chill.settings.green
import me.chill.utility.jda.embed
import net.dv8tion.jda.core.entities.Guild

fun generatePermission(commandName: String, guild: Guild) =
	if (hasPermission(commandName, guild.id)) Pair(commandName, getPermission(commandName, guild.id))
	else Pair(commandName, guild.roles[0].id)


fun generatePermissionsList(guild: Guild, commandNames: Array<String>) =
	commandNames
		.map {
			val permission = generatePermission(it, guild)
			"**${permission.first}** :: ${guild.getRoleById(permission.second).name}"
		}
		.joinToString("\n") { "- $it" }

fun listPermissionsEmbed(guild: Guild) =
	embed {
		title = "${guild.name} Permissions"
		color = green
		thumbnail = guild.iconUrl

		CommandContainer.commandSets.forEach {
			field {
				title = it.categoryName
				description = generatePermissionsList(guild, it.getCommandNames())
				inline = false
			}
		}
	}