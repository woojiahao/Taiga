package me.chill.events

import me.chill.database.operations.clearPermissions
import me.chill.database.operations.removeServerPreference
import me.chill.database.states.TargetChannel
import me.chill.exception.ListenerEventException
import me.chill.settings.lost
import me.chill.settings.red
import me.chill.utility.*
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnLeaveEvent : ListenerAdapter() {
  override fun onGuildMemberLeave(event: GuildMemberLeaveEvent?) {
    event ?: throw ListenerEventException(
      "On Member Leave",
      "Event object was null during member leave"
    )

    if (!event.guild.getMember(event.jda.selfUser).hasPermission(Permission.MESSAGE_WRITE)) return

    val serverId = event.guild.id
    val member = event.member

    val loggingChannel = event.guild.getTextChannelById(TargetChannel.Logging.get(serverId))
    if (!TargetChannel.Logging.isDisabled(serverId)) loggingChannel.send(memberLeaveEmbed(member))
  }

  override fun onGuildLeave(event: GuildLeaveEvent?) {
    event ?: throw ListenerEventException(
      "On Bot Leave",
      "Event object was null during bot leave"
    )

    val serverId = event.guild.id

    event.guild.jda.getTextChannelById("482338281946742786").send(
      successEmbed(
        "Server Leave",
        "Left ${event.guild.name}::$serverId on ${getDateTime()}"
      )
    )

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