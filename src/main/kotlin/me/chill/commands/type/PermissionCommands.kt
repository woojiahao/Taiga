package me.chill.commands.type

import me.chill.commands.arguments.types.RoleId
import me.chill.commands.arguments.types.Word
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.CommandContainer
import me.chill.commands.framework.commands
import me.chill.database.*
import me.chill.utility.embed
import me.chill.utility.failureEmbed
import me.chill.utility.successEmbed
import me.chill.settings.green
import net.dv8tion.jda.core.entities.Guild
import org.apache.commons.lang3.text.WordUtils

@CommandCategory
fun permissionCommands() = commands {
	name = "Permission"
	command("setpermission") {
		expects(Word(), RoleId())
		execute {
			val arguments = getArguments()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val commandName = arguments[0] as String
			val roleId = arguments[1] as String

			if (!CommandContainer.hasCommand(commandName)) {
				respond(
					failureEmbed(
						"Set Command Permission Failiure!",
						"Command: **$commandName** does not exist"
					)
				)
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
		expects(Word(), RoleId())
		execute {
			val arguments = getArguments()
			val guild = getGuild()

			val roles = guild.roles
			val serverId = guild.id

			val categoryName = WordUtils.capitalize(arguments[0] as String)
			val roleId = arguments[1] as String

			if (!CommandContainer.hasCategory(categoryName)) {
				respond(
					failureEmbed(
						"Set Category Permission Failure",
						"Category: **$categoryName** does not exist"
					)
				)
				return@execute
			}

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
				title = it.name
				description = generatePermissionsList(guild, it.getCommandNames())
				inline = false
			}
		}
	}