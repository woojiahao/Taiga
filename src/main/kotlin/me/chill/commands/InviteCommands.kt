package me.chill.commands

import me.chill.arguments.types.DiscordInvite
import me.chill.database.operations.addToWhitelist
import me.chill.database.operations.getWhitelist
import me.chill.database.operations.removeFromWhitelist
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.str
import me.chill.utility.successEmbed

@CommandCategory
fun inviteCommands() = commands("Invite") {
  command("addinvite") {
    expects(DiscordInvite())
    execute {
      val invite = arguments[0]!!.str()
      addToWhitelist(guild.id, invite)
      respond(
        successEmbed(
          "Invite Added To Whitelist",
          "Invite: $invite is now whitelisted and can be sent by any member",
          null
        )
      )
    }
  }

  command("removeinvite") {
    expects(DiscordInvite(true))
    execute {
      val invite = arguments[0]!!.str()
      removeFromWhitelist(guild.id, invite)
      respond(
        successEmbed(
          "Invite Removed From Whitelist",
          "Invite: $invite has been removed from the whitelist",
          null
        )
      )
    }
  }

  command("whitelist") {
    execute {
      val whitelist = getWhitelist(guild.id)
      respond(
        successEmbed(
          "${guild.name} Whitelist",
          if (whitelist.isBlank()) "No invites whitelisted"
          else whitelist,
          null
        )
      )
    }
  }
}