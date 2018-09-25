package me.chill.events

import me.chill.exception.ListenerEventException
import me.chill.roles.getRole
import me.chill.roles.hasRole
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.send
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnChannelCreationEvent : ListenerAdapter() {
	override fun onTextChannelCreate(event: TextChannelCreateEvent?) {
		event ?: throw ListenerEventException(
			"On Channel Creation",
			"Event object was null during channel creation"
		)

		if (!event.guild.hasRole("muted")) {
			event.channel.send(
				failureEmbed(
					"Channel Override Failed",
					"Unable to apply channel override for muted as role does not exist"
				)
			)
			return
		}
		event.channel.createPermissionOverride(event.guild.getRole("muted")).setDeny(Permission.MESSAGE_WRITE).queue()
	}
}