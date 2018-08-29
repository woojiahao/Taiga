package me.chill.commands.events

import me.chill.commands.framework.Command
import me.chill.commands.framework.CommandContainer
import me.chill.commands.framework.ContainerKeys
import me.chill.credential.Credentials
import me.chill.database.getPermission
import me.chill.database.hasPermission
import me.chill.exception.TaigaException
import me.chill.utility.settings.noWay
import me.chill.utility.settings.shock
import me.chill.logging.normalLog
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.settings.red
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*

class InputEvent(private val jda: JDA, private val credentials: Credentials) : ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event == null) throw TaigaException("Event object was null during message receive")

		if (event.member!!.user.isBot) return

		val message = event.message.contentRaw.trim()
		val messageChannel = event.channel
		val server = event.guild
		val invoker = server.getMemberById(event.author.id)

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

		val c = CommandContainer.getCommand(command) as Command

		val commandName = c.name
		if (!checkPermissions(commandName, server, invoker)) {
			messageChannel.send(insufficientPermissionEmbed(commandName))
			return
		}

		val expectedArgsSize = (c.args[ContainerKeys.Input] as Array<*>).size
		if (arguments.size != expectedArgsSize) {
			messageChannel.send(insufficientArgumentsEmbed(c.name, expectedArgsSize, arguments.size))
			return
		}

		c.run(jda, event.guild, event.member, messageChannel, arguments)
		normalLog(c)
	}
}

private fun checkPermissions(commandName: String, server: Guild, invoker: Member): Boolean {
	val serverId = server.id
	val everyoneRoleId = server.getRolesByName("@everyone", false)[0].id
	if (hasPermission(commandName, serverId)) {
		val expectedPermission = getPermission(commandName, serverId)
		val expectedPermissionPosition = server.getRoleById(expectedPermission).position

		val invokerRolelessHasPermission = invoker.roles.isNotEmpty() && invoker.roles[0].position < expectedPermissionPosition
		val invokerRoleHasPermission = invoker.roles.isEmpty() && expectedPermission != everyoneRoleId

		if (invokerRoleHasPermission || invokerRolelessHasPermission) {
			return false
		}
	} else {
		val highestRolePosition = server.roles[0].position
		val canInvoke = invoker.roles.isNotEmpty() && invoker.roles[0].position >= highestRolePosition
		if (!(invoker.isOwner || canInvoke)) {
			return false
		}
	}

	return true
}

private fun insufficientPermissionEmbed(commandName: String) =
	embed {
		title = "Insufficient Permission"
		description = "You cannot invoke **$commandName**, nice try"
		color = red
		thumbnail = noWay
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