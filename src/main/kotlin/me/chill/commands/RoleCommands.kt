package me.chill.commands

import me.chill.arguments.types.RoleId
import me.chill.arguments.types.UserId
import me.chill.database.preference.editJoinRole
import me.chill.database.preference.getJoinRole
import me.chill.database.preference.hasJoinRole
import me.chill.database.preference.removeJoinRole
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.assignRole
import me.chill.roles.removeRole
import me.chill.settings.clap
import me.chill.settings.green
import me.chill.settings.serve
import me.chill.utility.embed
import me.chill.utility.failureEmbed
import me.chill.utility.successEmbed
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role

@CommandCategory
fun roleCommands() = commands("Role") {
	command("roles") {
		execute {
			val guild = getGuild()

			val roles = guild.roles
			respond(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(RoleId(), UserId())
		execute {
			val arguments = getArguments()

			assignRole(getGuild(), getChannel(), arguments[0] as String, arguments[1] as String, false)
		}
	}

	command("unassign") {
		expects(RoleId(), UserId())
		execute {
			val arguments = getArguments()

			removeRole(getGuild(), getChannel(), arguments[0] as String, arguments[1] as String)
		}
	}

	command("getjoinrole") {
		execute {
			val guild = getGuild()
			val message =
				if (!hasJoinRole(guild.id)) {
					"**${guild.name}** currently does not have an auto-assigned role for new members"
				} else {
					val roleId = getJoinRole(guild.id)
					"New members will be assigned **${guild.getRoleById(roleId).name}** on join"
				}
			respond(successEmbed("Member On Join", message, serve))
		}
	}

	// todo: support multi-role assignment
	command("setjoinrole") {
		expects(RoleId())
		execute {
			val guild = getGuild()
			val roleId = getArguments()[0] as String

			val serverId = guild.id

			if (roleId == guild.getRolesByName("@everyone", false)[0].id) {
				respond(
					failureEmbed(
						"Member On Join",
						"You cannot assign that role to members on join!"
					)
				)
				return@execute
			}

			editJoinRole(serverId, roleId)

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
				if (!hasJoinRole(serverId)) {
					"**${server.name}** currently does not have an auto-assigned role for new members"
				} else {
					removeJoinRole(serverId)
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
