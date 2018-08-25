package me.chill.commands.container

import me.chill.commands.categories.administrationCommands
import me.chill.commands.categories.moderationCommands
import me.chill.commands.categories.permissionCommands
import me.chill.commands.categories.utilityCommands

class CommandContainer {
	init {
		utilityCommands()
		moderationCommands()
		administrationCommands()
		permissionCommands()
	}

	companion object {
		val commands = mutableListOf<Command>()

		fun hasCommand(command: String) = commands.stream().anyMatch { it.name == command }
		fun getCommand(command: String) = commands.stream().filter { it.name == command }.toArray()[0]!!
		fun getCommandNames() = commands.map { command -> command.name }.toTypedArray()
	}
}

fun command(name: String, create: Command.() -> Unit) {
	val command = Command(name)
	command.create()
	CommandContainer.commands.add(command)
}

// add a string arg for this to assign command category names
fun commands(cmds: () -> Unit) {
	cmds()
}
