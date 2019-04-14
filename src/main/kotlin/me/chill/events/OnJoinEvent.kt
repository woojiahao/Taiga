package me.chill.events

import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.exception.ListenerEventException
import me.chill.framework.CommandContainer
import me.chill.utility.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnJoinEvent : ListenerAdapter() {
  override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
    with(event) {
      this ?: throw ListenerEventException(
        "On Member Join",
        "Event object was null during member join"
      )

      if (!guild.getMember(jda.selfUser).hasPermission(Permission.MESSAGE_WRITE)) return

      val guildId = guild.id
      val joinChannel = guild.getTextChannelById(TargetChannel.JOIN.get(guildId))
      val loggingChannel = guild.getTextChannelById(TargetChannel.LOGGING.get(guildId))

      if (!TargetChannel.JOIN.isDisabled(guildId)) joinChannel.send(newMemberJoinEmbed(guild, member))

      if (hasJoinRole(guildId)) assignRole(guild, getJoinRole(guildId)!!, member.user.id)

      if (hasRaider(guildId, member.user.id)) {
        guild.getMutedRole() ?: loggingChannel.send(
          failureEmbed(
            "Mute Failed",
            "Unable to apply mute to user as the **muted** role does not exist, run `${getPrefix(guild.id)}setup`"
          )
        )
      } else {
        guild.addRoleToUser(member, guild.getMutedRole()!!)
      }
      loggingChannel.send(
        failureEmbed(
          "Raider Rejoin",
          "Raider: ${printMember(member)} as rejoined the server"
        )
      )
    }
  }


  override fun onGuildJoin(event: GuildJoinEvent?) {
    with(event) {
      this ?: throw ListenerEventException(
        "On Bot Join",
        "Event object was null during bot join"
      )

      val serverId = guild.id
      val defaultChannelId = guild.defaultChannel!!.id

      addServerPreference(serverId, defaultChannelId)
      loadGlobalPermissions(guild)
    }
  }
}

private fun loadGlobalPermissions(guild: Guild) {
  val globalCommands = CommandContainer.getGlobalCommands()
  val everyoneRoleId = guild.getRolesByName("@everyone", false)[0].id
  batchAddPermissions(globalCommands, guild.id, everyoneRoleId)
}