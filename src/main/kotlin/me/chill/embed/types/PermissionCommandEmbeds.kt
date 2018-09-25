package me.chill.embed.types

import me.chill.database.operations.viewPermissions
import me.chill.framework.CommandContainer
import me.chill.settings.green
import me.chill.utility.jda.embed
import net.dv8tion.jda.core.entities.Guild

data class Permission(val commandName: String, val permissionName: String)

fun generatePermissions(guild: Guild): Map<String, MutableList<Permission>> {
	val permissions = viewPermissions(guild.id)
	val highestRoleName = guild.roles[0].name
	val permissionMap = CommandContainer.commandSets.associate { it.categoryName to mutableListOf<Permission>() }

	CommandContainer
		.getCommandList()
		.asSequence()
		.distinctBy { it.name }
		.forEach {
			val name =
				if (!permissions.keys.contains(it.name)) highestRoleName
				else guild.getRoleById(permissions[it.name]!!).name
			permissionMap[it.category]?.add(Permission(it.name, name))
		}

	return permissionMap
}

fun listPermissionsEmbed(guild: Guild) =
	embed {
		title = "${guild.name} Permissions"
		color = green
		thumbnail = guild.iconUrl

		generatePermissions(guild).forEach { category, permissionList ->
			field {
				title = category
				description = permissionList.joinToString("\n") {
					"- **${it.commandName}** :: ${it.permissionName}"
				}
			}
		}
	}