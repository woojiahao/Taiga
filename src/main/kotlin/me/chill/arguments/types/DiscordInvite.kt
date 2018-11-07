package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.hasInviteInWhitelist
import me.chill.utility.isInvite
import net.dv8tion.jda.core.entities.Guild

class DiscordInvite(private val isExisting: Boolean = false) : Argument {
  override fun check(guild: Guild, arg: String) =
    when {
      !isInvite(arg) -> ArgumentParseMap(false, "$arg is not a valid Discord invite")
      isExisting && !hasInviteInWhitelist(guild.id, arg) -> ArgumentParseMap(false, "$arg is not a whitelisted Discord invite")
      else -> ArgumentParseMap(true, parsedValue = arg)
    }
}