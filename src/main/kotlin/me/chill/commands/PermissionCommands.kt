package me.chill.commands

import me.chill.arguments.types.CategoryName
import me.chill.arguments.types.CommandName
import me.chill.arguments.types.RoleId
import me.chill.database.operations.addPermission
import me.chill.database.operations.editPermission
import me.chill.database.operations.hasPermission
import me.chill.database.operations.removePermission
import me.chill.embed.types.listPermissionsEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.CommandContainer
import me.chill.framework.commands
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import org.apache.commons.lang3.text.WordUtils

@CommandCategory
fun permissionCommands() = commands("Permission") {
	command("setpermission") {
		expects(CommandName(), RoleId())
		execute {
			val roles = guild.roles
			val serverId = guild.id

			val commandName = arguments[0]!!.str()
			val roleId = arguments[1]!!.str()

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
					"Command: **$commandName** has been assigned to **${guild.getRoleById(roleId).name}**"
				)
			)
		}
	}

	command("viewpermissions") {
		execute {
			respond(listPermissionsEmbed(guild))
		}
	}

	command("setglobal") {
		expects(CategoryName())
		execute {
			val commandNames = CommandContainer.getCommandSet(arguments[0]!!.str()).getCommandNames()
			val everyoneRole = guild.getRolesByName("@everyone", false)[0]
			commandNames.forEach { name ->
				if (hasPermission(name, guild.id)) editPermission(name, guild.id, everyoneRole.id)
				else addPermission(name, guild.id, everyoneRole.id)
			}
			respond("All commands in **${arguments[0]!!.str()}** is now available to everyone")
		}
	}

	command("setpermissioncategory") {
		expects(CategoryName(), RoleId())
		execute {
			val roles = guild.roles
			val serverId = guild.id

			val categoryName = WordUtils.capitalize(arguments[0]!!.str())
			val roleId = arguments[1]!!.str()

			val highestRole = roles[0].id
			val commandSet = CommandContainer.getCommandSet(categoryName)
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