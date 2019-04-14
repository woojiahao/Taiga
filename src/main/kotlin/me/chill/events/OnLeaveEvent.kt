package me.chill.events

import me.chill.database.operations.clearPermissions
import me.chill.database.operations.removeServerPreference
import me.chill.database.states.TargetChannel
import me.chill.exception.ListenerEventException
import me.chill.settings.lost
import me.chill.settings.red
import me.chill.utility.embed
import me.chill.utility.printMember
import me.chill.utility.send
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnLeaveEvent : ListenerAdapter() {
  override fun onGuildMemberLeave(event: GuildMemberLeaveEvent?) {
    with(event) {
      this ?: throw ListenerEventException(
        "On Member Leave",
        "Event object was null during member leave"
      )

      if (!guild.getMember(jda.selfUser).hasPermission(Permission.MESSAGE_WRITE)) return

      val serverId = guild.id

      val loggingChannel = guild.getTextChannelById(TargetChannel.LOGGING.get(serverId))
      if (!TargetChannel.LOGGING.isDisabled(serverId)) loggingChannel.send(memberLeaveEmbed(member))
    }
  }

  override fun onGuildLeave(event: GuildLeaveEvent?) {
    event ?: throw ListenerEventException(
      "On Bot Leave",
      "Event object was null during bot leave"
    )

    val serverId = event.guild.id

    removeServerPreference(serverId)
    clearPermissions(serverId)
  }
}

private fun memberLeaveEmbed(member: Member) =
  embed {
    title = "Member leave"
    color = red
    description = "${printMember(member)} left the server"
    thumbnail = lost
  }