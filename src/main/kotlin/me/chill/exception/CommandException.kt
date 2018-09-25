package me.chill.exception

class CommandException(commandName: String, message: String) : TaigaException(
	"\n\n\tCommand: $commandName" +
		"\n\tReason: $message\n"
)