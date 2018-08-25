package me.chill.commands.categories

import me.chill.commands.container.CommandContainer
import me.chill.commands.container.command
import me.chill.commands.container.commands
import me.chill.database.*
import me.chill.gifs.happy
import me.chill.gifs.shock
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.red
import net.dv8tion.jda.core.entities.Guild

fun permissionCommands() = commands {
	command("setpermission") {
		expects(String, String)
		execute {
			val arguments = getArguments()
			val channel = getChannel()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val commandName = arguments[0]
			val roleId = arguments[1]

			if (!CommandContainer.hasCommand(commandName)) {
				channel.send(setPermissionFailureEmbed("Command: **$commandName** does not exist"))
				return@execute
			}

			if (guild.getRoleById(roleId) == null) {
				channel.send(setPermissionFailureEmbed("Role: **$roleId** does not exist on **${guild.name}**"))
				return@execute
			}

			val highestRole = roles[0].id
			if (roleId == highestRole) {
				removePermission(commandName, serverId)
			} else {
				if (hasPermission(commandName, serverId)) {
					editPermission(commandName, serverId, roleId)
				} else {
					addPermission(commandName, serverId, roleId)
				}
			}
			channel.send(
				setPermissionSuccessEmbed("Command: **$commandName** has been assigned to **${guild.getRoleById(roleId).name}**")
			)
		}
	}

	command("viewpermissions") {
		execute {
			val guild = getGuild()
			val channel = getChannel()

			val permissionMap = generatePermissionsMap(guild)
			val permissionsList = generatePermissionsList(guild, permissionMap.toSortedMap())
			channel.send(listPermissionsEmbed(guild, permissionsList))
		}
	}
}

private fun setPermissionStatusEmbed(status: String, color: Int,
									 thumbnail: String, message: String) =
	embed {
		title = "Set Permission $status!"
		this.color = color
		description = message
		this.thumbnail = thumbnail
	}

private fun setPermissionSuccessEmbed(message: String) =
	setPermissionStatusEmbed("Success", green, happy, message)

private fun setPermissionFailureEmbed(message: String) =
	setPermissionStatusEmbed("Failed", red, shock, message)

private fun generatePermissionsMap(guild: Guild): MutableMap<String, String> {
	val highestRole = guild.roles[0].id

	val assignedPermissions = viewPermissions(guild.id)
	val commandNames = CommandContainer.getCommandNames()

	val permissionMap = mutableMapOf<String, String>()
	permissionMap.putAll(assignedPermissions)
	commandNames.forEach { command ->
		if (!assignedPermissions.keys.contains(command)) permissionMap[command] = highestRole
	}

	return permissionMap
}

private fun generatePermissionsList(guild: Guild, permissionMap: Map<String, String>) =
	permissionMap
		.map { "**${it.key}** :: ${guild.getRoleById(it.value).name}" }
		.joinToString("\n") { "- $it" }


private fun listPermissionsEmbed(guild: Guild, message: String) =
	embed {
		title = "${guild.name} Permissions"
		color = green
		thumbnail = guild.iconUrl
		description = message
	}