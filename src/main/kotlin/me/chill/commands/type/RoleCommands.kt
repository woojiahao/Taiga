package me.chill.commands.type

import me.chill.commands.arguments.ArgumentType
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands
import me.chill.utility.jda.embed
import me.chill.utility.roles.assignRole
import me.chill.utility.roles.removeRole
import me.chill.utility.settings.green
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
		expects(ArgumentType.RoleId, ArgumentType.UserId)
		execute {
			val arguments = getArguments()

			assignRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String)
		}
	}

	command("unassign") {
		expects(ArgumentType.RoleId, ArgumentType.UserId)
		execute {
			val arguments = getArguments()

			removeRole(getGuild(), getChannel(), roleId = arguments[0] as String, targetId = arguments[1] as String)
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
