package me.chill.json.help

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import me.chill.commandInfo
import me.chill.commands.framework.Command
import me.chill.credentials
import java.io.File
import java.io.FileReader

private const val configDir = "config"
private const val helpFile = "help.json"
private val gson = GsonBuilder().create()

private const val configPath = "$configDir/$helpFile"

fun loadHelp(): List<CommandInfo> {
	val reader = JsonReader(FileReader(File(configPath)))
	val commandInfoList = gson
		.fromJson<JsonObject>(
			reader,
			JsonObject::class.java)
		.getAsJsonArray("commands")
	return commandInfoList.map {
		gson.fromJson<CommandInfo>(it, CommandInfo::class.java)
	}
}

fun findCommand(commandName: String) = commandInfo!!.first { info -> info.name == commandName }

fun Command.getSyntax() = "${credentials!!.prefix}${findCommand(name).syntax}"

fun Command.getExample() = "${credentials!!.prefix}${findCommand(name).example}"

fun Command.getCategory() = findCommand(name).category

fun Command.getDescription() = findCommand(name).description