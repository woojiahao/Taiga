package me.chill.commands.arguments.types

import me.chill.commands.arguments.Argument
import net.dv8tion.jda.core.entities.Guild

class WordArgument : Argument() {
	override fun check(guild: Guild, arg: String) = true
}