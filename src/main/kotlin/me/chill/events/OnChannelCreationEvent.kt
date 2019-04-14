package me.chill.events

import me.chill.exception.ListenerEventException
import me.chill.utility.failureEmbed
import me.chill.utility.getRole
import me.chill.utility.hasRole
import me.chill.utility.send
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnChannelCreationEvent : ListenerAdapter() {
  override fun onTextChannelCreate(event: TextChannelCreateEvent?) {
    with(event) {
      this ?: throw ListenerEventException(
        "On Channel Creation",
        "Event object was null during channel creation"
      )

      if (!guild.hasRole("muted")) {
        channel.send(
          failureEmbed(
            "Channel Override Failed",
            "Unable to apply channel override for muted as role does not exist"
          )
        )
        return
      }
      channel.createPermissionOverride(guild.getRole("muted")).setDeny(Permission.MESSAGE_WRITE).queue()
    }
  }
}