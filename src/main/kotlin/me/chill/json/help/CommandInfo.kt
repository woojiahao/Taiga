package me.chill.json.help

data class CommandInfo(
	val name: String,
	val description: String,
	val syntax: String,
	val example: String,
	var category: String? = null
)