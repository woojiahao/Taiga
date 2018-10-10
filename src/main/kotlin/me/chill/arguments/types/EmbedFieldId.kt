package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import net.dv8tion.jda.core.entities.Guild

class EmbedFieldId : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		TODO("not implemented")
	}
}