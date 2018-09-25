package me.chill.exception

class CommandException(commandName: String, message: String) : TaigaException(
	mapOf(
		"Command" to commandName,
		"Reason" to message
	)
)