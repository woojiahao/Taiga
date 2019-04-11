package me.chill.utility

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.chill.commandInfo
import me.chill.exception.CommandException
import me.chill.framework.Command
import me.chill.framework.CommandContainer
import java.io.File

data class ArgumentList(
  val name: String,
  val args: List<String>
)

data class CommandInfo(
  val name: String,
  val description: String,
  val syntax: String,
  val example: String,
  var category: String? = null,
  val argumentList: MutableList<ArgumentList>? = mutableListOf()
)

fun loadHelp(): List<CommandInfo> {
  val gson = Gson()
  val commandInfoList = mutableListOf<CommandInfo>()
  val helpJson = gson.read<JsonObject>(File("config/help.json").readText())

  helpJson.entrySet().forEach {
    it.value.asJsonArray.forEach { command ->
      commandInfoList += gson.read<CommandInfo>(command).apply { category = it.key }
    }
  }

  val loadedCommandNames = CommandContainer.getCommandList().map { it.name }
  val commandNames = commandInfoList.map { info -> info.name }
  loadedCommandNames.forEach {
    if (!commandNames.contains(it)) {
      throw CommandException(it, "Command not have a help in help.json")
    }
  }

  return commandInfoList
}

fun findCommand(commandName: String) = commandInfo.first { it.name == commandName }

val Command.syntax get() = "$serverPrefix${findCommand(name).syntax}"

val Command.example get() = "$serverPrefix${findCommand(name).example}"

val Command.description get() = findCommand(name).description

val Command.argumentList get() = findCommand(name).argumentList?.toList()