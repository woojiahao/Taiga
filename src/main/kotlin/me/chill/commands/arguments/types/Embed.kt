package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.ParseMap
import net.dv8tion.jda.core.entities.Guild

class Embed : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		println(arg)
		return ParseMap()
	}
}