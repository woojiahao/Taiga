package me.chill.events

import me.chill.arguments.parseArguments
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.embed.types.insufficientArgumentsEmbed
import me.chill.embed.types.invalidArgumentsEmbed
import me.chill.exception.ListenerEventException
import me.chill.framework.Command
import me.chill.framework.CommandContainer
import me.chill.framework.endArgumentList
import me.chill.logging.macroLog
import me.chill.logging.normalLog
import me.chill.raidManger
import me.chill.settings.noWay
import me.chill.utility.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter

private val permissionIgnoreList = arrayOf(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ)

class InputEvent : ListenerAdapter() {
  override fun onMessageReceived(event: MessageReceivedEvent?) {
    handleInput(event)
  }
}

private fun handleInput(event: MessageReceivedEvent?) {
  with(event) {
    this ?: throw ListenerEventException("User Input", "event is null")

    member ?: return
    if (member.user.isBot) return

    val message = message.contentRaw.trim()

    val guildPrefix = getPrefix(guild.id)

    handleRaider(member, guild, channel) ?: return

    if (!handleInvite(message, member, guild, channel, this.message)) return

    if (!message.startsWith(guildPrefix)) return

    val (isSilentInvoke, commandParts) = extractCommandParts(message, guildPrefix)
    val attemptedCommandMacro = commandParts[0]

    if (attemptedCommandMacro.isBlank() || !attemptedCommandMacro[0].isLetterOrDigit()) return

    if (hasMacro(guild.id, attemptedCommandMacro)) {
      if (commandParts.size == 1) {
        channel.send(getMacro(guild.id, attemptedCommandMacro))
        if (!TargetChannel.LOGGING.isDisabled(guild.id))
          macroLog(attemptedCommandMacro, member, channel, guild)
      }
      return
    }

    if (!CommandContainer.hasCommand(attemptedCommandMacro)) {
      channel.send(
        failureEmbed(
          "Invalid Command/Macro",
          "Command/Macro: **$attemptedCommandMacro** does not exist"
        )
      )
      return
    }

    val commands = CommandContainer.getCommand(attemptedCommandMacro)

    if (!checkPermissions(attemptedCommandMacro, guild, member)) {
      channel.send(
        failureEmbed(
          "Insufficient Permission", "You cannot invoke **$attemptedCommandMacro**, nice try", noWay
        )
      )
      return
    }

    val selection = matchCommand(commands, commandParts)
    if (selection == null) {
      channel.send(
        insufficientArgumentsEmbed(
          guildPrefix,
          attemptedCommandMacro,
          commands.map { it.argumentTypes.size }.sorted().toTypedArray()
        )
      )
      return
    }

    var (arguments, selectedCommand) = selection

    if (selectedCommand.argumentTypes.isNotEmpty()) {
      val parseMap = parseArguments(selectedCommand, guild, arguments)
      if (!parseMap.status) {
        channel.send(invalidArgumentsEmbed(guildPrefix, selectedCommand, parseMap.errMsg))
        return
      }

      arguments = parseMap.parsedValues
    }

    try {
      this.message.addReaction("\uD83D\uDC40").complete()
      selectedCommand.run(guildPrefix, jda, guild, member, channel, arguments)
      if (!TargetChannel.LOGGING.isDisabled(guild.id))
        normalLog(selectedCommand)

      if (isSilentInvoke)
        this.message.delete().complete()
    } catch (e: InsufficientPermissionException) {
      if (permissionIgnoreList.contains(e.permission)) return

      channel.send(
        failureEmbed(
          "Failed to invoke command",
          "You need to give me the permission to **${e.permission.getName()}** to use **$attemptedCommandMacro**"
        )
      )
    }
  }
}

private fun extractCommandParts(message: String, guildPrefix: String): Pair<Boolean, List<String>> {
  val isSilentInvoke = message.startsWith(guildPrefix.repeat(2))
  val prefixLength = if (!isSilentInvoke) 1 else 2
  return Pair(isSilentInvoke, message.substring(prefixLength).split(" ").toList())
}

private fun matchCommand(
  commandList: List<Command>,
  commandParts: List<String>
): Pair<List<String>, Command>? {
  for (command in commandList) {
    val expectedArgSize = command.argumentTypes.size
    val compiledArguments = formArguments(commandParts, command) ?: return null
    if (compiledArguments.size != expectedArgSize) return null

    return Pair(compiledArguments, command)
  }

  return null
}

private fun handleInvite(
  message: String,
  invoker: Member,
  server: Guild,
  messageChannel: MessageChannel,
  originalMessage: Message
): Boolean {
  if (containsInvite(message)) {
    if (invoker.hasPermission(server, getInviteExcluded(server.id), "Administrator", "Moderator")) {
      return true
    }

    val extractedInvite = extractInvite(message)
    if (!hasInviteInWhitelist(server.id, extractedInvite)) {
      manageInviteSent(invoker, server, messageChannel, originalMessage)
      return false
    }
  }

  return true
}

private fun handleRaider(invoker: Member, server: Guild, messageChannel: MessageChannel) =
  when {
    invoker.hasPermission(server, getRaidRoleExcluded(server.id), "Administrator", "Moderator") -> true
    hasRaider(server.id, invoker.user.id) -> null
    else -> {
      raidManger.manageRaid(server, messageChannel, invoker)
      false
    }
  }

private fun checkPermissions(commandName: String, server: Guild, invoker: Member): Boolean {
  val intendedPermission = if (hasPermission(commandName, server.id)) {
    getPermission(commandName, server.id)
  } else {
    server.roles[0].id
  }
  return invoker.hasPermission(server, intendedPermission)
}

private fun formArguments(commandParts: List<String>, command: Command): List<String>? =
  with(commandParts) {
    if (size <= 1) return emptyList()

    val argTypes = command.argumentTypes
    val hasEndArguments = argTypes.any { endArgumentList.contains(it::class.java) }

    return if (hasEndArguments) {
      val sentenceArgPosition = argTypes.size

      if (size - 1 < sentenceArgPosition) return null

      subList(1, sentenceArgPosition).toMutableList().apply {
        this += subList(sentenceArgPosition, size).joinToString(" ")
      }
    } else subList(1, this.size)
  }

