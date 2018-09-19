package me.chill.framework

import me.chill.exception.TaigaException
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	init {
		val reflections = Reflections("me.chill.commands", MethodAnnotationsScanner())
		reflections
			.getMethodsAnnotatedWith(CommandCategory::class.java)
			.forEach { it.invoke(null) }

		commandList().forEach { command ->
			val overloadedCommands = getCommand(command.name)
			overloadedCommands.forEach { overloadedCommand ->
				if (overloadedCommand != command) {
					if (overloadedCommand.argumentTypes.size == command.argumentTypes.size) {
						throw TaigaException("Unable to overload command: ${command.name} with the same number of argument types")
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

		fun getSet(category: String) = commandSets.stream().filter { it.categoryName == category }.toArray()[0]!! as CommandSet

		fun getCommand(command: String) = commandList().filter { it.name == command }.toTypedArray()

		fun getCommandNames() = commandList().map { it.name }.toTypedArray()

		fun getGlobalCommands() = commandList().asSequence().filter { it.getGlobal() }.map { it.name }.toList()

		fun commandList() = commandSets.map { it.commands }.flatten()
	}
}

inline fun commands(categoryName: String, create: CommandSet.() -> Unit) {
	val set = CommandSet(categoryName)
	set.create()
	CommandContainer.commandSets.add(set)
}