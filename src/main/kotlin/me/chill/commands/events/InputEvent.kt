package me.chill.commands.events

import me.chill.commands.container.Command
import me.chill.commands.container.CommandContainer
import me.chill.commands.container.ContainerKeys
import me.chill.credential.Credentials
import me.chill.exception.TaigaException
import me.chill.gifs.shock
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.red
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*

class InputEvent(private val jda: JDA, private val credentials: Credentials) : ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event == null) throw TaigaException("Event object was null during message receive")

		if (event.member!!.user.isBot) return

		val message = event.message.contentRaw.trim()
		val messageChannel = event.channel

		if (!message.startsWith(credentials.prefix!!)) return

		val commandParts = message.substring(credentials.prefix!!.length).split(" ").toTypedArray()
		val command = commandParts[0]
		var arguments: Array<String> = emptyArray()
		if (commandParts.size > 1) {
			arguments = Arrays.copyOfRange(commandParts, 1, commandParts.size)
		}

		if (!CommandContainer.hasCommand(command)) {
			messageChannel.send(invalidCommandEmbed(command))
			return
		}

		val c: Command = CommandContainer.getCommand(command) as Command
		val expectedArgsSize = (c.args[ContainerKeys.Input] as Array<*>).size

		if (arguments.size != expectedArgsSize) {
			messageChannel.send(insufficientArgumentsEmbed(c.name, expectedArgsSize, arguments.size))
			return
		}

		c.run(jda, event.guild, event.member, messageChannel, arguments)
	}
}

private fun insufficientArgumentsEmbed(commandName: String, expected: Int, actual: Int) =
	embed {
		title = "Insufficient Arguments"
		description = "Command: **$commandName** requires **$expected** arguments, you gave **$actual** arguments"
		color = red
		thumbnail = shock
	}

private fun invalidCommandEmbed(command: String) =
	embed {
		title = "Invalid Command"
		description = "Command: **$command** does not exist"
		color = red
		thumbnail = shock
	}