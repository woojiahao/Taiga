package me.chill.arguments

data class ArgumentParseMap(
	val status: Boolean,
	val errMsg: String = "",
	val parsedValue: String = ""
)