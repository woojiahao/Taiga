package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.database.states.WelcomeState
import me.chill.database.disableWelcome
import me.chill.database.editWelcomeMessage
import me.chill.database.enableWelcome
import me.chill.database.getWelcomeDisabled
import me.chill.events.newMemberJoinEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.clap
import me.chill.utility.failureEmbed
import me.chill.utility.send
import me.chill.utility.successEmbed
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

@CommandCategory
fun welcomeCommands() = commands("Welcome") {

	command("disablewelcome") {
		execute {
			alterWelcomeState(getGuild(), getChannel(), WelcomeState.Disabled)
		}
	}

	command("enablewelcome") {
		execute {
			alterWelcomeState(getGuild(), getChannel(), WelcomeState.Enabled)
		}
	}

	command("getwelcomeenabled") {
		execute {
			val guild = getGuild()
			val isWelcomeDisabled = getWelcomeDisabled(guild.id)
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
			respond(newMemberJoinEmbed(getGuild(), getInvoker()))
		}
	}

	command("setwelcomemessage") {
		expects(Sentence())
		execute {
			editWelcomeMessage(getGuild().id, getArguments()[0] as String)
			respond(newMemberJoinEmbed(getGuild(), getInvoker()))
		}
	}
}

private fun alterWelcomeState(guild: Guild, channel: MessageChannel, welcomeState: WelcomeState) {
	val guildId = guild.id
	val isWelcomeDisabled = getWelcomeDisabled(guildId)
	val welcomeStateName = welcomeState.name

	if (isWelcomeDisabled != welcomeState.b) {
		when (welcomeState) {
			WelcomeState.Disabled -> disableWelcome(guildId)
			WelcomeState.Enabled -> enableWelcome(guildId)
		}
		channel.send(
			successEmbed(
				"${welcomeStateName.substring(0, welcomeStateName.length)} Welcome",
				"Welcomes have been ${welcomeStateName.toLowerCase()} for **${guild.name}**",
				clap
			)
		)
	} else {
		channel.send(
			failureEmbed(
				"${welcomeStateName.substring(0, welcomeStateName.length)} Welcome",
				"Welcomes are already ${welcomeStateName.toLowerCase()} for **${guild.name}**"
			)
		)
	}
}