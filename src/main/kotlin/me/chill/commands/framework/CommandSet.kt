package me.chill.commands.framework

class CommandSet {
	val commands = mutableListOf<Command>()
	var name = "Empty"

	fun command(name: String, create: Command.() -> Unit) {
		val command = Command(name, this.name)
		command.create()
		commands.add(command)
	}

	fun hasCommand(command: String) = commands.stream().anyMatch { it.name == command }
}