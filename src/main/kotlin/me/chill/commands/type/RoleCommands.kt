package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType
import me.chill.commands.arguments.ArgumentType.RoleId
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.database.editOnJoinRole
import me.chill.database.getOnJoinRole
import me.chill.database.hasOnJoinRole
import me.chill.database.setOnJoinRole
import me.chill.utility.jda.embed
import me.chill.utility.roles.assignRole
import me.chill.utility.roles.removeRole
import me.chill.utility.settings.clap
import me.chill.utility.settings.green
import me.chill.utility.settings.serve
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role

@CommandCategory
fun roleCommands() = commands {
	name = "Role"

	command("roles") {
		execute {
			val guild = getGuild()

			val roles = guild.roles
			respond(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(RoleId, ArgumentType.UserId)
		execute {
			val arguments = getArguments()

			assignRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String, silent = false)
		}
	}

	command("unassign") {
		expects(RoleId, ArgumentType.UserId)
		execute {
			val arguments = getArguments()

			removeRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String)
		}
	}

	command("getjoinrole") {
		execute {
			val guild = getGuild()
			var message = ""
			message =
				if (!hasOnJoinRole(guild.id)) {
					"**${guild.name}** currently does not have an auto-assigned role for new members"
				} else {
					val roleId = getOnJoinRole(guild.id)
					"New members will be assigned **${guild.getRoleById(roleId).name}** on join"
				}
			respond(getOnJoinRoleEmbed(message))
		}
	}

	// todo: support multi-role assignment
	command("setjoinrole") {
		expects(RoleId)
		execute {
			val guild = getGuild()
			val roleId = getArguments()[0] as String

			val serverId = guild.id

			if (hasOnJoinRole(serverId)) editOnJoinRole(serverId, roleId)
			else setOnJoinRole(serverId, roleId)

			respond(setOnJoinRoleEmbed(guild.getRoleById(roleId).name))
		}
	}
}

private fun setOnJoinRoleEmbed(roleName: String) =
	embed {
		title = "Member On Join"
		color = green
		thumbnail = clap
		description = "New members will be assigned **$roleName** on join"
	}

private fun getOnJoinRoleEmbed(message: String) =
	embed {
		title = "Member On Join"
		color = green
		description = message
		thumbnail = serve
	}

private fun listRolesEmbed(guild: Guild, roles: List<Role>) =
	embed {
		title = "Roles in ${guild.name}"
		color = green
		description = roles.joinToString("\n") {
			"${it.name} :: ${it.id}"
		}
		thumbnail = guild.iconUrl
	}
