package me.chill.embed.types

import me.chill.database.operations.ServerPreference
import me.chill.database.states.TargetChannel
import me.chill.settings.cyan
import me.chill.utility.embed
import me.chill.utility.printChannel
import net.dv8tion.jda.api.entities.Guild

fun preferenceEmbed(guild: Guild, preference: ServerPreference) =
  embed {
    title = "${guild.name} Preferences"
    color = cyan
    thumbnail = guild.iconUrl
    description = "The following are the preferences set for ${guild.name}(${preference.serverId})\n\n" +
      "An extensive guide on how to customize preferences for Taiga can be found [here](https://woojiahao.github.io/Taiga/#/setting_preferences)"

    field {
      title = "Prefix"
      description = "`${preference.prefix}` causes a regular command invoke\n" +
        "`${preference.prefix.repeat(2)}` causes a silent command invoke"
    }

    field {
      title = "Join Channel"
      description = "${printChannel(guild.getTextChannelById(preference.joinChannel))}\n" +
        "Join messages are currently **${TargetChannel.disableStatus(preference.welcomeDisabled)}**"
    }

    field {
      title = "Logging Channel"
      description = "${printChannel(guild.getTextChannelById(preference.loggingChannel))}\n" +
        "Command logging is currently **${TargetChannel.disableStatus(preference.loggingDisabled)}**"
    }

    field {
      title = "Suggestion Channel"
      description = "${printChannel(guild.getTextChannelById(preference.suggestionChannel))}\n" +
        "Suggestion system is currently **${TargetChannel.disableStatus(preference.suggestionDisabled)}**"
    }

    field {
      title = "User Activity Tracking Channel"
      description = "${printChannel(guild.getTextChannelById(preference.userActivityChannel))}\n" +
        "User activity tracking system is currently **${TargetChannel.disableStatus(preference.userActivityTrackingDisabled)}**"
    }

    field {
      val raidMessage = StringBuilder("Users who send **${preference.messageLimit} messages** " +
        "within **${preference.messageDuration} seconds** will be caught as a raider.")
      preference.roleExcluded?.let {
        raidMessage.append("\nMembers with the **${guild.getRoleById(it).name}** role and higher will be excluded from raid control.")
      }
      title = "Raid"
      description = raidMessage.toString()
    }

    field {
      val isWelcomeDisabled = TargetChannel.disableStatus(preference.welcomeDisabled)
      val newMemberMessage = StringBuilder("Welcomes are **$isWelcomeDisabled** for the server.\n")
      preference.onJoinRole?.let {
        newMemberMessage.append("New members will be assigned **${guild.getRoleById(it).name}** on join.\n")
      }
      newMemberMessage.append("New members will be greeted with: **${preference.welcomeMessage}**")

      title = "New Members"
      description = newMemberMessage.toString()
    }

    field {
      val message = StringBuilder("Members who send **5** invites will be automatically banned.")
      preference.inviteExcluded?.let {
        message.append("\nMembers with the role **${guild.getRoleById(preference.inviteExcluded).name} and higher** will not be caught for sending invites")
      }
      title = "Invites"
      description = message.toString()
    }

    field {
      title = "Time Multiplier"
      description = "Any moderation action that involves time will have a duration of **${preference.timeMultiplier.fullTerm}s**"
    }
  }