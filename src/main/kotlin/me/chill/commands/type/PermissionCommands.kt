package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType.RoleId
import me.chill.commands.arguments.ArgumentType.Word
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.CommandContainer
import me.chill.commands.framework.commands
import me.chill.database.*
import me.chill.utility.jda.embed
import me.chill.utility.settings.green
import me.chill.utility.settings.happy
import me.chill.utility.settings.red
import me.chill.utility.settings.shock
import net.dv8tion.jda.core.entities.Guild

@CommandCategory
fun permissionCommands() = commands {
	name = "Permission"
	command("setpermission") {
		expects(Word, RoleId)
		execute {
			val arguments = getArguments()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val commandName = arguments[0] as String
			val roleId = arguments[1] as String

			if (!CommandContainer.hasCommand(commandName)) {
				respond(setPermissionFailureEmbed("Command: **$commandName** does not exist"))
				return@execute
			}

			if (guild.getRoleById(roleId) == null) {
				respond(setPermissionFailureEmbed("Role: **$roleId** does not exist on **${guild.name}**"))
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
			respond(
				setPermissionSuccessEmbed("Command: **$commandName** has been assigned to **${guild.getRoleById(roleId).name}**")
			)
		}
	}

	command("viewpermissions") {
		execute {
			respond(listPermissionsEmbed(getGuild()))
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

private fun generatePermission(commandName: String, guild: Guild) =
	if (hasPermission(commandName, guild.id)) Pair(commandName, getPermission(commandName, guild.id))
	else Pair(commandName, guild.roles[0].id)


private fun generatePermissionsList(guild: Guild, commandNames: Array<String>) =
	commandNames
		.map {
			val permission = generatePermission(it, guild)
			"${permission.first} :: ${guild.getRoleById(permission.second).name}"
		}
		.joinToString("\n") { "- $it" }


private fun listPermissionsEmbed(guild: Guild) =
	embed {
		title = "${guild.name} Permissions"
		color = green
		thumbnail = guild.iconUrl

		CommandContainer.commandSets.forEach {
			field {
				title = it.name
				description = generatePermissionsList(guild, it.getCommandNames())
				inline = false
			}
		}
	}