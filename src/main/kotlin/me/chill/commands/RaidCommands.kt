package me.chill.commands

import me.chill.arguments.types.UserId
import me.chill.database.operations.freeAll
import me.chill.database.operations.freeRaider
import me.chill.database.operations.getPrefix
import me.chill.database.operations.getRaiders
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.clap
import me.chill.utility.*

@CommandCategory
fun raidCommands() = commands("Raid") {
  command("viewraiders") {
    execute {
      val raiders = getRaiders(guild.id)
      respond(
        successEmbed(
          "Raiders in ${guild.name}",
          if (raiders.isEmpty()) "No raiders on the server" else raiders.joinToString(", "),
          null
        )
      )
    }
  }

  command("freeraider") {
    expects(UserId(true))
    execute {
      val userId = arguments[0]!!.str()

      if (guild.getMutedRole() == null) {
        respond(
          failureEmbed(
            "Unmute Failure",
            "Unable to free raider as the muted role does not exist, run `${getPrefix(guild.id)}setup`"
          )
        )
        return@execute
      }

      if (guild.getMemberById(userId) != null) {
        removeRole(guild, guild.getMutedRole()!!.id, userId)
      }
      freeRaider(guild.id, userId)
      respond(
        successEmbed(
          "Raider Freed",
          "Raider: **$userId** has been freed",
          clap
        )
      )
    }
  }

  command("freeallraiders") {
    execute {
      if (guild.getMutedRole() == null) {
        respond(
          failureEmbed(
            "Unmute Failure",
            "Unable to free raider as the muted role does not exist, run `${getPrefix(guild.id)}setup`"
          )
        )
        return@execute
      }

      val mutedRoleId = guild.getMutedRole()!!.id

      getRaiders(guild.id)
        .filter { raider -> guild.getMemberById(raider) != null }
        .forEach { member -> removeRole(guild, mutedRoleId, member) }
      freeAll(guild.id)
      respond(
        successEmbed(
          "All Raiders Freed",
          "All raiders in **${guild.name}** have been freed",
          clap
        )
      )
    }
  }
}