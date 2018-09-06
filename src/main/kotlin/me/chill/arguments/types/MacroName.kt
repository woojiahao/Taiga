package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import me.chill.database.operations.hasMacro
import me.chill.framework.CommandContainer
import net.dv8tion.jda.core.entities.Guild

class MacroName(private val isExisting: Boolean = false) : Argument {
	override fun check(guild: Guild, arg: String): ParseMap {
		if (CommandContainer.hasCommand(arg)) {
			return ParseMap(false, "Macro: **$arg** is an existing command name")
		}

		if (!isExisting) {
			if (hasMacro(guild.id, arg)) {
				return ParseMap(false, "Macro: **$arg** already exists")
			}
		} else {
			if (!hasMacro(guild.id, arg)) {
				return ParseMap(false, "Macro: **$arg** does not exist")
			}
		}

		return ParseMap(parseValue = arg)
	}
}