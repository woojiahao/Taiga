package me.chill.events

import me.chill.database.states.TargetChannel
import me.chill.exception.ListenerEventException
import me.chill.settings.yellow
import me.chill.utility.embed
import me.chill.utility.hasMember
import me.chill.utility.send
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnUserActivityEvent : ListenerAdapter() {
  override fun onUserUpdateName(event: UserUpdateNameEvent?) {
    with(event) {
      this ?: throw ListenerEventException("On User Update Name", "null event")

      val userGuilds = jda.guilds.filter { it.hasMember(user) }.filterNotNull()
      userGuilds.forEach {
        if (!TargetChannel.USER_ACTIVITY.isDisabled(it.id)) {
          val logging = it.getTextChannelById(TargetChannel.USER_ACTIVITY.get(it.id))
          logging.send(changeNameEmbed(oldName, newName))
        }
      }
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