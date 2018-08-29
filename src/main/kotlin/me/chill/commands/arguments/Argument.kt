package me.chill.commands.arguments

import net.dv8tion.jda.core.entities.Guild

open class Argument {
	open fun check(guild: Guild, arg: String): Boolean = false
}