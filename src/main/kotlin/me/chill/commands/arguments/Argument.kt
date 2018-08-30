package me.chill.commands.arguments

import net.dv8tion.jda.core.entities.Guild

// todo: make more informative error messages
interface Argument {
	fun check(guild: Guild, arg: String): ParseMap
}