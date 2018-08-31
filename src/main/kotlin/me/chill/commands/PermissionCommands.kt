package me.chill.commands

import me.chill.arguments.types.CategoryName
import me.chill.arguments.types.CommandName
import me.chill.arguments.types.RoleId
import me.chill.database.*
import me.chill.framework.CommandCategory
import me.chill.framework.CommandContainer
import me.chill.framework.commands
import me.chill.settings.green
import me.chill.utility.embed
import me.chill.utility.successEmbed
import net.dv8tion.jda.core.entities.Guild
import org.apache.commons.lang3.text.WordUtils

@CommandCategory
fun permissionCommands() = commands("Permission") {
	command("setpermission") {
		expects(CommandName(), RoleId())
		execute {
			val arguments = getArguments()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val commandName = arguments[0] as String
			val roleId = arguments[1] as String

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
				successEmbed(
					"Set Command Permission Success!",
					"Command: **$commandName** has been assigned to **${guild.getRoleById(roleId).name}**")
			)
		}
	}

	command("viewpermissions") {
		execute {
			respond(listPermissionsEmbed(getGuild()))
		}
	}

	command("setpermissioncategory") {
		expects(CategoryName(), RoleId())
		execute {
			val arguments = getArguments()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val categoryName = WordUtils.capitalize(arguments[0] as String)
			val roleId = arguments[1] as String
			
			val highestRole = roles[0].id
			val commandSet = CommandContainer.getSet(categoryName)
			commandSet.commands.forEach { command ->
				val commandName = command.name
				if (roleId == highestRole) {
					removePermission(commandName, serverId)
				} else {
					if (hasPermission(commandName, serverId)) {
						editPermission(commandName, serverId, roleId)
					} else {
						addPermission(commandName, serverId, roleId)
					}
				}
			}
			respond(
				successEmbed(
					"Set Category Permission Success!",
					"All commands in **$categoryName** has been assigned to **${guild.getRoleById(roleId).name}**")
			)
		}
	}
}

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
				title = it.categoryName
				description = generatePermissionsList(guild, it.getCommandNames())
				inline = false
			}
		}
	}