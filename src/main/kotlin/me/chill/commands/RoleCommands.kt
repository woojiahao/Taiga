package me.chill.commands

import me.chill.arguments.types.RoleId
import me.chill.arguments.types.UserId
import me.chill.database.operations.hasJoinRole
import me.chill.database.operations.removeJoinRole
import me.chill.embed.types.listRolesEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.assignRole
import me.chill.utility.removeRole
import me.chill.utility.successEmbed
import me.chill.utility.str

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