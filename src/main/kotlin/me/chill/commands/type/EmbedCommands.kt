package me.chill.commands.type

import me.chill.commands.arguments.types.Embed
import me.chill.commands.framework.CommandCategory
import me.chill.commands.framework.commands

@CommandCategory
fun embedCategory() = commands("Embed") {
	command("embed") {
		expects(Embed())
		execute {

		}
	}
}