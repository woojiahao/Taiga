package me.chill.commands.framework

import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	init {
		val reflections = Reflections("me.chill.commands.type", MethodAnnotationsScanner())
		reflections
			.getMethodsAnnotatedWith(CommandSet::class.java)
			.forEach {
				addCategory(it.name)
				it.invoke(null)
			}
	}

	companion object {
		private val commands = mutableMapOf<String, MutableList<Command>>()

		fun loadContainer(): CommandContainer = CommandContainer()

		fun addCategory(category: String) {
			commands[category] = mutableListOf()
		}

		fun addCommand(command: Command) = commands.add(command)
		fun hasCommand(command: String) = commands.stream().anyMatch { it.name == command }
		fun getCommand(command: String) = commands.stream().filter { it.name == command }.toArray()[0]!!
		fun getCommandNames() = commands.map { command -> command.name }.toTypedArray()
	}
}

fun command(name: String, create: Command.() -> Unit) {
	val command = Command(name)
	command.create()
	CommandContainer.addCommand(command)
}

fun commands(func: () -> Unit) {
	func()
}
