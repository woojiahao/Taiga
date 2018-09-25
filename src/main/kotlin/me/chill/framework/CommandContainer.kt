package me.chill.framework

import me.chill.exception.CommandException
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	init {
		val reflections = Reflections("me.chill.commands", MethodAnnotationsScanner())
		reflections
			.getMethodsAnnotatedWith(CommandCategory::class.java)
			.forEach { it.invoke(null) }

		getCommandList().forEach { command ->
			if (!command.name[0].isLetterOrDigit()) {
				throw CommandException("Command name must start with a letter or digit")
			}
			val overloadedCommands = getCommand(command.name)
			overloadedCommands.forEach { overloadedCommand ->
				if (overloadedCommand != command) {
					if (overloadedCommand.argumentTypes.size == command.argumentTypes.size) {
						throw CommandException("Unable to overload command: ${command.name} with the same number of argument types")
					}
				}
			}
		}
	}

	companion object {
		val commandSets = mutableListOf<CommandSet>()

		fun loadContainer() {
			CommandContainer()
		}

		fun hasCommand(command: String) = commandSets.stream().anyMatch { it.hasCommand(command) }

		fun hasCategory(category: String) = commandSets.stream().anyMatch { it.categoryName == category }

		fun getCommandSet(category: String) =
			commandSets.stream().filter { it.categoryName == category }.toArray()[0]!! as CommandSet

		fun getCommand(command: String) = getCommandList().filter { it.name == command }.toTypedArray()

		fun getCommandNames() = getCommandList().map { it.name }.toTypedArray().distinct()

		fun getGlobalCommands() =
			getCommandList().asSequence().filter { it.getGlobal() }.map { it.name }.distinct().toList()

		fun getCommandList() =
			commandSets.map { it.commands }.flatten()
	}
}

inline fun commands(categoryName: String, create: CommandSet.() -> Unit) {
	val set = CommandSet(categoryName)
	set.create()
	set.commands.forEach { it.setGlobal(set.getGlobal()) }
	CommandContainer.commandSets.add(set)
}