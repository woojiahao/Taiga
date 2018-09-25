package me.chill.exception

class EndArgumentException(
	commandName: String,
	reason: String
) : TaigaException(
	"\n\n\tCommand: $commandName" +
		"\n\tReason: $reason" +
		"\n\tFix: https://woojiahao.github.io/Taiga/#/argument_types?id=end-arguments\n"
)