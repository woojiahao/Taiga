package me.chill.exception

class ArgumentException(argumentType: String, message: String) : TaigaException(
	mapOf(
		"Argument Type" to argumentType,
		"Reason" to message
	)
)