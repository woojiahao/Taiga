package me.chill.framework

import me.chill.arguments.types.ArgumentMix
import me.chill.exception.CommandException
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	companion object {
		val commandSets = mutableListOf<CommandSet>()

		fun loadContainer() {
			val reflections = Reflections("me.chill.commands", MethodAnnotationsScanner())
			reflections.getMethodsAnnotatedWith(CommandCategory::class.java).forEach { it.invoke(null) }
			checkCommands()
		}

		fun hasCommand(command: String) = commandSets.stream().anyMatch { it.hasCommand(command) }

		fun hasCategory(category: String) = commandSets.stream().anyMatch { it.categoryName == category }

		fun getCommandSet(category: String) =
			commandSets.stream().filter { it.categoryName == category }.toArray()[0]!! as CommandSet

		fun getCommand(command: String) = getCommandList().filter { it.name == command }.toTypedArray()

		fun getCommandNames() = getCommandList().map { it.name }.toTypedArray().distinct()

		fun getGlobalCommands() =
			getCommandList().asSequence().filter { it.getGlobal() == true }.map { it.name }.distinct().toList()

		fun getCommandList() =
			commandSets.map { it.commands }.flatten()
	}
}

inline fun commands(categoryName: String, create: CommandSet.() -> Unit) {
	val set = CommandSet(categoryName)
	set.create()
	for (command in set.commands) {
		if (command.getGlobal() == null) {
			command.setGlobal(set.getGlobal())
		} else {
			command.getGlobal()?.let {
				command.setGlobal(it)
			}
		}
	}
	CommandContainer.commandSets.add(set)
}

private fun checkCommands() {
	CommandContainer.getCommandList().forEach {
		it.getAction() ?: throw CommandException(it.name, "All commands must implement an execute body")

		if (!it.name[0].isLetterOrDigit()) {
			throw CommandException(it.name, "Command name must start with a letter or digit")
		}

		if (it.name == it.category.toLowerCase()) {
			throw CommandException(
				it.name,
				"Command names should not be the same as a category name"
			)
		}

		if (it.argumentTypes.any { arg -> arg.javaClass == ArgumentMix::class.java }) {
			val argMix = it.argumentTypes.filter { arg -> arg.javaClass == ArgumentMix::class.java }[0] as ArgumentMix
			if (argMix.arguments.distinctBy { mix -> mix.javaClass }.size != argMix.arguments.size) {
				throw CommandException(
					it.name,
					"Unable to create an ArgumentMix of the same argument type"
				)
			}
		}

		val overloadedCommands = CommandContainer.getCommand(it.name)
		overloadedCommands.forEach { overloadedCommand ->
			if (overloadedCommand != it) {
				if (overloadedCommand.argumentTypes.size == it.argumentTypes.size) {
					throw CommandException(
						it.name,
						"Unable to overload command with the same number of argument types: ${overloadedCommand.argumentTypes.size}"
					)
				}
			}
		}
	}
}