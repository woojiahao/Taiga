package me.chill.commands.container

class CommandContainer {
	companion object {
		val commands = mutableListOf<Command>()

		fun findCommand(command: String) = commands.stream().anyMatch { it.name == command }
	}
}