package me.chill.commands

import me.chill.arguments.types.Integer
import me.chill.arguments.types.RoleId
import me.chill.arguments.types.UserId
import me.chill.database.operations.*
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.roles.removeRole
import me.chill.settings.clap
import me.chill.settings.serve
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.getMutedRole
import me.chill.utility.int
import me.chill.utility.jda.successEmbed

@CommandCategory
fun raidCommands() = commands("Raid") {
	command("setraidmessageduration") {
		expects(Integer(1))
		execute {
			val guild = getGuild()
			val duration = getArguments()[0]!!.int()
			editRaidMessageDuration(guild.id, duration)
			respond(
				successEmbed(
					"Raid Message Duration",
					"Raid message duration for **${guild.name}** has been set to **$duration** seconds"
				)
			)
		}
	}

	command("setraidmessagelimit") {
		expects(Integer(1))
		execute {
			val guild = getGuild()
			val limit = getArguments()[0]!!.int()
			editRaidMessageLimit(guild.id, limit)
			respond(
				successEmbed(
					"Raid Message Limit",
					"Raid message limit for **${guild.name}** has been set to **$limit** messages"
				)
			)
		}
	}

	command("setraidroleexcluded") {
		expects(RoleId())
		execute {
			val guild = getGuild()
			val roleId = getArguments()[0] as String
			editRaidRoleExcluded(guild.id, roleId)
			respond(
				successEmbed(
					"Raid Role Excluded",
					"**${guild.getRoleById(roleId).name} and higher** will be excluded from the raid filter"
				)
			)
		}
	}

	command("getraidmessageduration") {
		execute {
			val guild = getGuild()
			respond(
				successEmbed(
					"Raid Message Duration",
					"The raid message duration for **${guild.name}** is **${getRaidMessageDuration(guild.id)}** seconds",
					serve
				)
			)
		}
	}

	command("getraidmessagelimit") {
		execute {
			val guild = getGuild()
			respond(
				successEmbed(
					"Raid Message Limit",
					"The raid message limit for **${guild.name}** is **${getRaidMessageLimit(guild.id)}** messages",
					serve
				)
			)
		}
	}

	command("getraidroleexcluded") {
		execute {
			val guild = getGuild()
			val raidRoleExcluded = getRaidRoleExcluded(guild.id)
			respond(
				successEmbed(
					"Raid Role Excluded",
					if (raidRoleExcluded == null) "No role is being filtered"
					else "**${guild.getRoleById(raidRoleExcluded).name} and higher** are excluded from the raid filter",
					serve
				)
			)
		}
	}

	command("viewraiders") {
		execute {
			val guild = getGuild()
			val raiders = getRaiders(guild.id)
			respond(
				successEmbed(
					"Raiders in ${guild.name}",
					raiders.joinToString(", "),
					guild.iconUrl
				)
			)
		}
	}

	command("freeraider") {
		expects(UserId(true))
		execute {
			val guild = getGuild()
			val userId = getArguments()[0] as String

			if (guild.getMutedRole() == null) {
				respond(
					failureEmbed(
						"Unmute Failure",
						"Unable to free raider as the muted role does not exist, run `${getPrefix(guild.id)}setup`"
					)
				)
				return@execute
			}

			if (guild.getMemberById(userId) != null) {
				if (!removeRole(guild, getChannel(), guild.getMutedRole()!!.id, userId)) return@execute
			}
			freeRaider(guild.id, userId)
			respond(
				successEmbed(
					"Raider Freed",
					"Raider: **$userId** has been freed",
					clap
				)
			)
		}
	}

	command("freeallraiders") {
		execute {
			val guild = getGuild()
			val channel = getChannel()

			if (guild.getMutedRole() == null) {
				respond(
					failureEmbed(
						"Unmute Failure",
						"Unable to free raider as the muted role does not exist, run `${getPrefix(guild.id)}setup`"
					)
				)
				return@execute
			}

			val mutedRoleId = guild.getMutedRole()!!.id

			getRaiders(guild.id)
				.filter { raider -> guild.getMemberById(raider) != null }
				.forEach { member -> removeRole(guild, channel, mutedRoleId, member) }
			freeAll(guild.id)
			respond(
				successEmbed(
					"All Raiders Freed",
					"All raiders in **${guild.name}** have been freed",
					clap
				)
			)
		}
	}
}