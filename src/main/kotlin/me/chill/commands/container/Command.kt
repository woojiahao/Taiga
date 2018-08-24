package me.chill.commands.container

class Command {
	var name = ""
		private set

	fun arguments() {

	}

	fun execute() {

	}
}

fun command(create: Command.() -> Unit) {
	val command = Command()
	command.create()
	CommandContainer.commands.add(command)
}