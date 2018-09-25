package me.chill.exception

class ListenerEventException(eventName: String, message: String) : TaigaException(
	mapOf(
		"Event" to eventName,
		"Reason" to message
	)
)