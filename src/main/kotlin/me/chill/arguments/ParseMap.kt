package me.chill.arguments

data class ParseMap(var status: Boolean = true,
					var errMsg: String = "",
					val parsedValues: MutableList<String> = mutableListOf())