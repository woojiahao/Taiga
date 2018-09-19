package me.chill.framework

import me.chill.exception.TaigaException

class CommandSet(val categoryName: String) {
	val commands = mutableListOf<Command>()

	inline fun command(name: String, create: Command.() -> Unit) {
		val command = Command(name)
		command.create()
		val invalidOverride = hasCommand(name)
				&& commands.asSequence()
				.filter { it.name == name }
				.any { it.argumentTypes.size == command.argumentTypes.size }

		if (invalidOverride) throw TaigaException("Unable to overload command: $name with the same number of argument types")
		commands.add(command)
	}

	fun hasCommand(command: String) = commands.stream().anyMatch { it.name == command }

	fun getCommandNames() = commands.map { it.name }.toTypedArray().sortedArray()
}