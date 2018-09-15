package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.utility.jda.findUser
import net.dv8tion.jda.core.entities.Guild

class UserIdList(private val globalSearch: Boolean = false,
				 private val pipes: Char = ',') : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		arg.split(pipes).forEach {
			if (it.toLongOrNull() == null) {
				return@check ArgumentParseMap(false, "ID: **$it** is not valid")
			}

			if (globalSearch) {
				if (guild.jda.findUser(it) == null) {
					return@check ArgumentParseMap(false, "User by the ID of **$it** does not exist on Discord")
				}
			} else {
				if (guild.getMemberById(it) == null) {
					return@check ArgumentParseMap(false, "Member by the ID of **$it** is not found")
				}
			}
		}

		return ArgumentParseMap(true, parsedValue = arg)
	}
}