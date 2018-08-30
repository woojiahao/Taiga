package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType
import me.chill.commands.arguments.ArgumentType.RoleId
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.database.*
import me.chill.utility.jda.embed
import me.chill.utility.jda.successEmbed
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
			respond(successEmbed("Member On Join", message, serve))
		}
	}

	// todo: support multi-role assignment
	command("setjoinrole") {
		expects(RoleId)
		execute {
			val guild = getGuild()
			val roleId = getArguments()[0] as String

			val serverId = guild.id

			if (roleId == guild.getRolesByName("@everyone", false)[0].id) {

			}

			if (hasOnJoinRole(serverId)) editOnJoinRole(serverId, roleId)
			else setOnJoinRole(serverId, roleId)

			respond(
				successEmbed(
					"Member On Join",
					"New members will be assigned **${guild.getRoleById(roleId).name}** on join",
					clap
				)
			)
		}
	}

	command("clearjoinrole") {
		execute {
			val server = getGuild()
			val serverId = server.id
			respond(successEmbed(
				"Member On Join",
				if (!hasOnJoinRole(serverId)) {
					"**${server.name}** currently does not have an auto-assigned role for new members"
				} else {
					removeOnJoinRole(serverId)
					"New members will now no longer be assigned a default role"
				}
			))
		}
	}
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
