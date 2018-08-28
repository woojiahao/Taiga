package me.chill.commands.framework

import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	init {
		val reflections = Reflections("me.chill.commands.type", MethodAnnotationsScanner())
		val annotatedFunctions = reflections.getMethodsAnnotatedWith(CommandSet::class.java)
		annotatedFunctions.forEach { it.invoke(null) }
	}

	companion object {
		private val commands = mutableListOf<Command>()

		fun loadContainer(): CommandContainer = CommandContainer()

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

// add a string arg for this to assign command category names
fun commands(cmds: () -> Unit) {
	cmds()
}
