package me.chill.exception

class ListenerEventException(
	eventName: String, message: String
) : TaigaException(
	"\n\n\tEvent: $eventName" +
		"\n\tReason: $message\n"
)