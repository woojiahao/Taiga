package me.chill.commands

import me.chill.arguments.types.Integer
import me.chill.database.preference.editRaidMessageDuration
import me.chill.database.preference.editRaidMessageLimit
import me.chill.database.preference.getRaidMessageDuration
import me.chill.database.preference.getRaidMessageLimit
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.serve
import me.chill.utility.int
import me.chill.utility.successEmbed

@CommandCategory
fun raidCommands() = commands("Raid") {
	command("setraidmessageduration") {
		expects(Integer(1))
		execute {
			val guild = getGuild()
			val duration = getArguments()[0]!!.int()
			editRaidMessageDuration(guild.id, duration)
			respond (
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
			respond (
				successEmbed(
					"Raid Message Limit",
					"Raid message limit for **${guild.name}** has been set to **$limit** messages"
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
}