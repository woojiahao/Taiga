package me.chill.exception

class EndArgumentException(commandName: String, reason: String) : TaigaException(
	mapOf(
		"Command" to commandName,
		"Reason" to reason,
		"Fix" to "https://woojiahao.github.io/Taiga/#/argument_types?id=end-arguments"
	)
)