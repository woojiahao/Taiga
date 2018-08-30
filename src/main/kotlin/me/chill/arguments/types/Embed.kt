package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Embed : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		println(arg)
		return ParseMap()
	}
}