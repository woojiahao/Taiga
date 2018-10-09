package me.chill.events

import me.chill.database.states.TargetChannel
import me.chill.exception.ListenerEventException
import me.chill.settings.yellow
import me.chill.utility.embed
import me.chill.utility.hasMember
import me.chill.utility.send
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnUserActivityEvent : ListenerAdapter() {
	override fun onUserUpdateName(event: UserUpdateNameEvent?) {
		event ?: throw ListenerEventException("On User Update Name", "null event")

		for (guild in event.jda.guilds.asSequence().filter { it.hasMember(event.user) }.filterNotNull().toList()) {
			if (TargetChannel.Useractivity.isDisabled(guild.id)) continue

			val logging = guild.getTextChannelById(TargetChannel.Useractivity.get(guild.id))
			logging.send(changeNameEmbed(event.oldName, event.newName))
		}
	}
}

private fun changeNameEmbed(oldName: String, newName: String) =
	embed {
		title = "Name Change"
		color = yellow
		field {
			title = "Old Name"
			description = oldName
		}

		field {
			title = "New Name"
			description = newName
		}
	}