package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ArgumentParseMap
import me.chill.utility.jda.findUser
import net.dv8tion.jda.core.entities.Guild

class UserIdList(private val globalSearch: Boolean = false,
				 private val pipes: Char = ',') : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		val parsed = arg.split(pipes).map {
			if (it.startsWith("<@") && it.endsWith(">")) arg.substring(2, arg.length - 1)
			else it
		}

		parsed.forEach {
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

		return ArgumentParseMap(true, parsed.joinToString(pipes.toString()))
	}
}