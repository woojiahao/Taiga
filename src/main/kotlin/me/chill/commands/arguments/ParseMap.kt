package me.chill.commands.arguments

data class ParseMap(var status: Boolean = true,
					var errMsg: String = "",
					val parseValue: String = "",
					val parsedValues: MutableList<String> = mutableListOf())