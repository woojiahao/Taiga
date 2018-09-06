package me.chill.arguments.types

import me.chill.arguments.Argument
import me.chill.arguments.ParseMap
import me.chill.framework.CommandContainer
import net.dv8tion.jda.core.entities.Guild
import org.apache.commons.lang3.text.WordUtils

class CategoryName : Argument {
	override fun check(guild: Guild, arg: String) =
		if (!CommandContainer.hasCategory(WordUtils.capitalize(arg))) ParseMap(false, "Category: **$arg** does not exist")
		else ParseMap(parseValue = WordUtils.capitalize(arg))
}