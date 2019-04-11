package me.chill.commands

import me.chill.arguments.types.*
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.database.states.TimeMultiplier
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.embed.types.preferenceEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.clap
import me.chill.utility.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import org.apache.commons.lang3.text.WordUtils

private enum class Preferences(val lowercase: String) {
  Prefix("prefix"), Multiplier("multiplier"), Logging("logging"),
  Join("join"), Suggestion("suggestion"), Useractivity("useractivity"),
  MessageLimit("messagelimit"), MessageDuration("messageduration"),
  RaidExcluded("raidexcluded"), WelcomeMessage("welcomemessage"),
  JoinRole("joinrole"), InviteExcluded("inviteexcluded");

  companion object {
    val names
      get() = values().map { it.name.toLowerCase() }

    fun match(attempt: String) = values().first { it.lowercase == attempt }
  }
}

@CommandCategory
fun administrationCommands() = commands("Administration") {
  command("setchannel") {
    expects(Word(TargetChannel.names))
    execute {
      val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
      type.edit(guild.id, channel.id)
      respond(
        cleanEmbed(
          "Channel Assigned",
          "${type.name} channel has been assigned to <#${channel.id}> in **${guild.name}**"
        )
      )
    }
  }

  command("setup") {
    execute {
      setupMuted(guild)
      respond(
        successEmbed(
          "Set-Up Completed",
          "Bot has been set up for **${guild.name}**\n" +
              "Remember to move the `muted` role higher in order for it to take effect"
        )
      )
    }
  }

  command("disable") {
    expects(Word(TargetChannel.names))
    execute {
      val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
      type.disable(guild.id)
      respond(
        successEmbed(
          "${type.name} Disabled",
          "${type.name}s have been disabled for **${guild.name}**",
          clap
        )
      )
    }
  }

  command("enable") {
    expects(Word(TargetChannel.names))
    execute {
      val type = TargetChannel.valueOf(WordUtils.capitalize(arguments[0]!!.str()))
      type.enable(guild.id)
      respond(
        successEmbed(
          "${type.name} Enabled",
          "${type.name}s have been enabled for **${guild.name}**",
          clap
        )
      )
    }
  }

  command("preferences") {
    execute {
      respond(preferenceEmbed(guild, getAllPreferences(guild.id)))
    }
  }

  command("get") {
    expects(Word(Preferences.names))
    execute {
      respond(displayPreference(Preferences.match(arguments[0]!!.str()), guild, invoker))
    }
  }

  command("set") {
    expects(Word(Preferences.names), Sentence())
    execute {
      respond(setPreference(Preferences.match(arguments[0]!!.str()), arguments[1]!!.str(), guild, invoker, jda))
    }
  }
}

private fun setPreference(preference: Preferences, input: String, guild: Guild, invoker: Member, jda: JDA): MessageEmbed? {
  return when (preference) {
    Preferences.Prefix -> {
      val parseMap = Prefix().check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to change prefix", parseMap.errMsg)
      }

      setPrefix(input, guild.getMember(jda.selfUser), guild)
      cleanEmbed("${guild.name} Prefix Changed", "Prefix has been changed to **$input**")
    }
    Preferences.Multiplier -> {
      val parseMap = Word(TimeMultiplier.names).check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to change multiplier", parseMap.errMsg)
      }

      setMultiplier(input, guild.id)
      cleanEmbed("${guild.name} Multiplier Changed", "Multiplier has been changed to **$input**")
    }
    Preferences.Logging, Preferences.Join, Preferences.Suggestion, Preferences.Useractivity -> {
      val type = TargetChannel.valueOf(WordUtils.capitalize(preference.lowercase))
      val parseMap = ChannelId().check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set channel", parseMap.errMsg)
      }

      type.edit(guild.id, parseMap.parsedValue)
      cleanEmbed(
        "Channel Assigned",
        "${type.name} channel has been assigned to <#${parseMap.parsedValue}> in **${guild.name}**"
      )
    }

    Preferences.MessageLimit -> {
      val parseMap = Integer(1).check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set raid message limit", parseMap.errMsg)
      }

      editRaidMessageLimit(guild.id, input.toInt())
      cleanEmbed(
        "Raid Message Limit",
        "Raid message limit for **${guild.name}** has been set to **${input.toInt()}** messages"
      )
    }

    Preferences.MessageDuration -> {
      val parseMap = Integer(1).check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set raid message duration", parseMap.errMsg)
      }

      editRaidMessageDuration(guild.id, input.toInt())
      cleanEmbed(
        "Raid Message Duration",
        "Raid message duration for **${guild.name}** has been set to **${input.toInt()}** seconds"
      )
    }
    Preferences.RaidExcluded -> {
      val parseMap = RoleId().check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set raid role excluded", parseMap.errMsg)
      }

      editRaidRoleExcluded(guild.id, parseMap.parsedValue)
      cleanEmbed(
        "Raid Role Excluded",
        "**${guild.getRoleById(parseMap.parsedValue).name} and higher** will be excluded from the raid filter"
      )
    }

    Preferences.WelcomeMessage -> {
      editWelcomeMessage(guild.id, input)
      newMemberJoinEmbed(guild, invoker)
    }

    Preferences.JoinRole -> {
      val parseMap = RoleId().check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set member on join role", parseMap.errMsg)
      }

      val roleId = parseMap.parsedValue
      if (roleId == guild.getRolesByName("@everyone", false)[0].id) {
        return failureEmbed(
          "Unable to set member on join role",
          "You cannot assign that role to members on join!"
        )
      }

      editJoinRole(guild.id, roleId)
      cleanEmbed(
        "Member On Join",
        "New members will be assigned **${guild.getRoleById(roleId).name}** on join"
      )
    }

    Preferences.InviteExcluded -> {
      val parseMap = RoleId().check(guild, input)
      if (!parseMap.status) {
        return failureEmbed("Unable to set invite role excluded", parseMap.errMsg)
      }

      editInviteExcluded(guild.id, parseMap.parsedValue)
      cleanEmbed(
        "Invite Role Excluded",
        "**${guild.getRoleById(parseMap.parsedValue).name} and higher** will be excluded from the invite filter"
      )
    }
  }

}

private fun displayPreference(preference: Preferences, guild: Guild, invoker: Member): MessageEmbed? {
  val id = guild.id
  val name = guild.name
  return when (preference) {
    Preferences.Prefix -> cleanEmbed("$name Prefix", "Current prefix is: **${getPrefix(id)}**")
    Preferences.Multiplier ->
      cleanEmbed(
        "Time Multiplier",
        "Current time multiplier for **$name** is in **${getTimeMultiplier(id).fullTerm}s**")
    Preferences.Logging, Preferences.Join, Preferences.Suggestion, Preferences.Useractivity -> {
      val targetChannel = TargetChannel.valueOf(WordUtils.capitalize(preference.lowercase))
      val targetName = targetChannel.name

      return cleanEmbed(
        "$targetName Channel",
        "Current ${targetName.toLowerCase()} channel is ${printChannel(guild.getTextChannelById(targetChannel.get(guild.id)))}\n" +
            "${targetName}s are currently **${targetChannel.disableStatus(guild.id)}**"
      )!!
    }

    Preferences.MessageLimit -> cleanEmbed(
      "Raid Message Limit",
      "The raid message limit for **$name** is **${getRaidMessageLimit(id)}** messages")

    Preferences.MessageDuration -> cleanEmbed(
      "Raid Message Duration",
      "The raid message duration for **$name** is **${getRaidMessageDuration(id)}** seconds")

    Preferences.RaidExcluded -> {
      val raidRoleExcluded = getRaidRoleExcluded(guild.id)
      cleanEmbed(
        "Raid Role Excluded",
        if (raidRoleExcluded == null) "No role is being filtered"
        else "**${guild.getRoleById(raidRoleExcluded).name} and higher** are excluded from the raid filter")
    }

    Preferences.WelcomeMessage -> newMemberJoinEmbed(guild, invoker)

    Preferences.JoinRole -> {
      val message =
        if (!hasJoinRole(guild.id)) {
          "**${guild.name}** currently does not have an auto-assigned role for new members"
        } else {
          val roleId = getJoinRole(guild.id)
          "New members will be assigned **${guild.getRoleById(roleId).name}** on join"
        }
      cleanEmbed("Member On Join", message)
    }

    Preferences.InviteExcluded -> {
      val inviteRoleExcluded = getInviteExcluded(guild.id)
      cleanEmbed(
        "Invite Role Excluded",
        if (inviteRoleExcluded == null) "No role is being filtered"
        else "**${guild.getRoleById(inviteRoleExcluded).name} and higher** are excluded from the invite filter")
    }
  }
}

private fun setupMuted(guild: Guild) {
  val mutedName = "muted"
  if (!guild.hasRole(mutedName)) guild.createRole(mutedName)

  val muted = guild.getRole(mutedName)

  guild.textChannels.forEach {
    val hasOverride = it.rolePermissionOverrides.any { perm ->
      perm.role.name.toLowerCase() == mutedName.toLowerCase()
    }

    if (!hasOverride) {
      it.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE).queue()
    }
  }
}

private fun setMultiplier(multiplier: String, guildId: String) {
  val timeMultiplier = when (multiplier) {
    "m", "s", "h", "d" -> TimeMultiplier.valueOf(multiplier.toUpperCase())
    "minute", "second", "hour", "day" -> TimeMultiplier.values().first { mul -> mul.fullTerm == multiplier }
    else -> TimeMultiplier.M
  }

  editTimeMultiplier(guildId, timeMultiplier)
}

private fun setPrefix(newPrefix: String, botAsMember: Member, guild: Guild) {
  val originalNickname = if (botAsMember.nickname != null) {
    botAsMember.nickname.substring(0, botAsMember.nickname.lastIndexOf("("))
  } else {
    botAsMember.effectiveName
  }
  editPrefix(guild.id, newPrefix)
  guild.controller.setNickname(guild.getMemberById(botAsMember.user.id), "$originalNickname (${newPrefix}help)").complete()
}