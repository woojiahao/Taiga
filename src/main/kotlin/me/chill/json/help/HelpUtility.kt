package me.chill.json.help

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.chill.commandInfo
import me.chill.exception.TaigaException
import me.chill.framework.Command
import me.chill.framework.CommandContainer
import java.io.File
import java.io.FileReader


fun loadHelp(): List<CommandInfo> {
	val gson = Gson()
	val list = mutableListOf<CommandInfo>()
	val commandInfoList = gson.fromJson<JsonObject>(FileReader(File("config/help.json")), JsonObject::class.java)

	commandInfoList
		.entrySet()
		.map { gson.fromJson(it.value, JsonArray::class.java) }
		.forEach {
			it.forEach { info -> list.add(gson.fromJson(info, CommandInfo::class.java)) }
		}
	CommandContainer.commandList().forEach {
		if (!list.asSequence().map { info -> info.name }.contains(it.name)) {
			throw TaigaException("Command: ${it.name} does not have a help in help.json")
		}
	}
	return list
}

fun findCommand(commandName: String) = commandInfo!!.first { it.name == commandName }

val Command.syntax get() = "$serverPrefix${findCommand(name).syntax}"

val Command.example get() = "$serverPrefix${findCommand(name).example}"

val Command.category get() = findCommand(name).category

val Command.description get() = findCommand(name).description