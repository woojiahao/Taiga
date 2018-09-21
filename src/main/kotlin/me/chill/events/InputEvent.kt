package me.chill.events

import me.chill.arguments.parseArguments
import me.chill.arguments.types.Sentence
import me.chill.credentials
import me.chill.database.operations.*
import me.chill.embed.types.insufficientArgumentsEmbed
import me.chill.embed.types.invalidArgumentsEmbed
import me.chill.exception.TaigaException
import me.chill.framework.Command
import me.chill.framework.CommandContainer
import me.chill.logging.normalLog
import me.chill.raidManger
import me.chill.settings.noWay
import me.chill.utility.invite.containsInvite
import me.chill.utility.invite.extractInvite
import me.chill.utility.invite.manageInviteSent
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.send
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*

class InputEvent : ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent?) {
		event ?: throw TaigaException("Event object was null during message receive")

		if (event.member == null || event.member!!.user.isBot) return

		val message = event.message.contentRaw.trim()
		val messageChannel = event.channel
		val server = event.guild
		val invoker = event.member

		val serverPrefix = getPrefix(server.id)

		handleRaider(invoker, server, messageChannel) ?: return

		if (containsInvite(message)) {
			val extractedInvite = extractInvite(message)
			if (!hasInviteInWhitelist(server.id, extractedInvite) && !invoker.isOwner) {
				manageInviteSent(invoker, server, messageChannel, event.message)
				return
			}
		}

		if (!message.startsWith(serverPrefix)) return

		val silentInvoke = message.startsWith(serverPrefix.repeat(2))

		val commandParts = if (!silentInvoke) {
			message.substring(serverPrefix.length).split(" ").toTypedArray()
		} else {
			message.substring(serverPrefix.repeat(2).length).split(" ").toTypedArray()
		}
		val attemptedCommandMacro = commandParts[0]

		if (attemptedCommandMacro.isBlank()) return

		if (hasMacro(server.id, attemptedCommandMacro)) {
			if (commandParts.size == 1) messageChannel.send(getMacro(server.id, attemptedCommandMacro))
			return
		}

		if (!CommandContainer.hasCommand(attemptedCommandMacro)) {
			messageChannel.send(
				failureEmbed(
					"Invalid Command/Macro",
					"Command/Macro: **$attemptedCommandMacro** does not exist"
				)
			)
			return
		}

		val commands = CommandContainer.getCommand(attemptedCommandMacro)

		if (!checkPermissions(attemptedCommandMacro, server, invoker)) {
			messageChannel.send(
				failureEmbed(
					"Insufficient Permission",
					"You cannot invoke **$attemptedCommandMacro**, nice try",
					thumbnail = noWay
				)
			)
			return
		}

		var arguments: Array<String>? = null
		var selectedCommand: Command? = null
		for (i in 0 until commands.size) {
			val command = commands[i]
			val expectedArgSize = command.argumentTypes.size
			val compiledArguments = formArguments(commandParts, command)
			if (compiledArguments != null && compiledArguments.size == expectedArgSize) {
				arguments = compiledArguments
				selectedCommand = command
				break
			}
		}

		if (arguments == null || selectedCommand == null) {
			messageChannel.send(
				insufficientArgumentsEmbed(
					serverPrefix,
					attemptedCommandMacro,
					commands.map { it.argumentTypes.size }.toTypedArray()
				)
			)
			return
		}

		if (selectedCommand.argumentTypes.isNotEmpty()) {
			val parseMap = parseArguments(selectedCommand, server, arguments)
			if (!parseMap.status) {
				messageChannel.send(invalidArgumentsEmbed(serverPrefix, selectedCommand, parseMap.errMsg))
				return
			}

			arguments = parseMap.parsedValues.toTypedArray()
		}

		try {
			event.message.addReaction("\uD83D\uDC40").complete()
			selectedCommand.run(serverPrefix, event.jda, event.guild, event.member, messageChannel, arguments)
			normalLog(selectedCommand)
			if (silentInvoke) {
				event.message.delete().complete()
			}
		} catch (e: InsufficientPermissionException) {
			if (e.permission == Permission.MESSAGE_READ) return
			messageChannel.send(
				failureEmbed(
					"Failed to invoke command",
					"You need to give me the permission to **${e.permission.getName()}** to use **$attemptedCommandMacro**"
				)
			)
		}
	}
}

private fun handleRaider(invoker: Member, server: Guild, messageChannel: MessageChannel): Boolean? {
	val isExcludedFromRaidControl =
		invoker.roles.isNotEmpty()
			&& getRaidRoleExcluded(server.id) != null
			&& invoker.roles[0].position >= server.getRoleById(getRaidRoleExcluded(server.id)).position
	val isAlreadyCaught = hasRaider(server.id, invoker.user.id)
	return when {
		isExcludedFromRaidControl -> true
		isAlreadyCaught -> null
		else -> {
			raidManger!!.manageRaid(server, messageChannel, invoker)
			false
		}
	}
}

private fun checkPermissions(attemptedCommandMacro: String, server: Guild, invoker: Member): Boolean {
	val serverId = server.id
	val everyoneRoleId = server.getRolesByName("@everyone", false)[0].id
	if (hasPermission(attemptedCommandMacro, serverId)) {
		val expectedPermission = getPermission(attemptedCommandMacro, serverId)
		val expectedPermissionPosition = server.getRoleById(expectedPermission).position

		val roleHasPermission = invoker.roles.isNotEmpty() && invoker.roles[0].position < expectedPermissionPosition
		val rolelessHasPermission = invoker.roles.isEmpty() && expectedPermission != everyoneRoleId

		if (rolelessHasPermission || roleHasPermission) {
			return false
		}
	} else {
		val highestRolePosition = server.roles[0].position
		val canInvoke = invoker.roles.isNotEmpty() && invoker.roles[0].position >= highestRolePosition
		val isBotOwner = invoker.user.id == credentials!!.botOwnerId
		if (!(isBotOwner || invoker.isOwner || canInvoke)) return false
	}

	return true
}

private fun formArguments(commandParts: Array<String>, c: Command): Array<String>? {
	var arguments = emptyArray<String>()
	if (commandParts.size > 1) {
		val argTypes = c.argumentTypes
		arguments = when {
			argTypes.any { it is Sentence } -> {
				val sentenceArgPosition = argTypes.size

				if (commandParts.size - 1 < sentenceArgPosition) return null

				val sentence = Arrays.copyOfRange(commandParts, sentenceArgPosition, commandParts.size).joinToString(" ")

				val tempArgs = Arrays.copyOfRange(commandParts, 1, sentenceArgPosition).toMutableList()
				tempArgs.add(sentence)
				tempArgs.toTypedArray()
			}
			else -> Arrays.copyOfRange(commandParts, 1, commandParts.size)
		}
	}

	return arguments
}