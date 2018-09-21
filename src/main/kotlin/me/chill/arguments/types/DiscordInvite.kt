package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.utility.invite.isInvite
import net.dv8tion.jda.core.entities.Guild

class DiscordInvite : Argument {
	override fun check(guild: Guild, arg: String) =
		if (!isInvite(arg)) {
			ArgumentParseMap(false, "$arg is not a valid Discord invite")
		} else {
			ArgumentParseMap(true, parsedValue = arg)
		}
}