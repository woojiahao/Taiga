package me.chill.arguments

import net.dv8tion.jda.core.entities.Guild

interface Argument {
  fun check(guild: Guild, arg: String): ArgumentParseMap
}