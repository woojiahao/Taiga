package me.chill.commands.container

import me.chill.commands.categories.moderationCommands
import me.chill.commands.categories.utilityCommands

class CommandContainer {
	init {
		utilityCommands()
		moderationCommands()
	}

	companion object {
		val commands = mutableListOf<Command>()

		fun hasCommand(command: String) = commands.stream().anyMatch { it.name == command }
		fun getCommand(command: String) = commands.stream().filter { it.name == command }.toArray()[0]!!
	}
}

fun command(name: String, create: Command.() -> Unit) {
	val command = Command(name)
	command.create()
	CommandContainer.commands.add(command)
}

fun commands(cmds: () -> Unit) {
	cmds()
}
