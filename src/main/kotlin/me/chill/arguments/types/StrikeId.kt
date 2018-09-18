package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.database.operations.hasStrike
import me.chill.utility.int
import net.dv8tion.jda.core.entities.Guild

class StrikeId : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		if (!hasStrike(guild.id, arg.int())) {
			return ArgumentParseMap(false, "Strike ID: **$arg** does not exist in the server")
		}

		return ArgumentParseMap(true, parsedValue = arg)
	}
}