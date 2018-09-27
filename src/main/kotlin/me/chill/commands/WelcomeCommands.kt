package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.database.operations.LoggingType
import me.chill.database.operations.editWelcomeMessage
import me.chill.database.states.WelcomeState
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.clap
import me.chill.utility.jda.successEmbed
import me.chill.utility.str

@CommandCategory
fun welcomeCommands() = commands("Welcome") {
	command("getwelcomeenabled") {
		execute {
			val isWelcomeDisabled = LoggingType.Welcome.isDisabled(guild.id)
			val welcomeState = when (isWelcomeDisabled) {
				false -> WelcomeState.Enabled
				true -> WelcomeState.Disabled
			}
			respond(
				successEmbed(
					"Welcomes",
					"Welcomes are ${welcomeState.name.toLowerCase()} in **${guild.name}**",
					clap
				)
			)
		}
	}

	command("getwelcomemessage") {
		execute {
			respond(newMemberJoinEmbed(guild, invoker))
		}
	}

	command("setwelcomemessage") {
		expects(Sentence())
		execute {
			editWelcomeMessage(guild.id, arguments[0]!!.str())
			respond(newMemberJoinEmbed(guild, invoker))
		}
	}
}