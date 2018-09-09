package me.chill.commands

import me.chill.arguments.types.RoleId
import me.chill.arguments.types.UserId
import me.chill.database.operations.editJoinRole
import me.chill.database.operations.getJoinRole
import me.chill.database.operations.hasJoinRole
import me.chill.database.operations.removeJoinRole
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.assignRole
import me.chill.roles.removeRole
import me.chill.settings.clap
import me.chill.settings.green
import me.chill.settings.serve
import me.chill.utility.jda.embed
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role

@CommandCategory
fun roleCommands() = commands("Role") {
	command("roles") {
		execute {
			val roles = guild.roles
			respond(listRolesEmbed(guild, roles))
		}
	}

	command("assign") {
		expects(UserId(), RoleId())
		execute {
			assignRole(guild, channel, arguments[1]!!.str(), arguments[0]!!.str())
		}
	}

	command("unassign") {
		expects(UserId(), RoleId())
		execute {
			removeRole(guild, channel, arguments[1]!!.str(), arguments[0]!!.str())
		}
	}

	command("getjoinrole") {
		execute {
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
			val roleId = arguments[0]!!.str()

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
			respond(successEmbed(
				"Member On Join",
				if (!hasJoinRole(guild.id)) {
					"**${guild.name}** currently does not have an auto-assigned role for new members"
				} else {
					removeJoinRole(guild.id)
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
			"**${it.name}** :: ${it.id}"
		}
		thumbnail = guild.iconUrl
	}
