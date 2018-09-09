package me.chill.json.help

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import me.chill.commandInfo
import me.chill.framework.Command
import java.io.File
import java.io.FileReader

private val gson = GsonBuilder().create()

fun loadHelp(): List<CommandInfo> {
	val list = mutableListOf<CommandInfo>()
	val reader = JsonReader(FileReader(File("config/help.json")))
	val commandInfoList = gson.fromJson<JsonObject>(reader, JsonObject::class.java)
	commandInfoList
		.entrySet()
		.map { gson.fromJson(it.value, JsonArray::class.java) }
		.forEach {
			it.forEach { info -> list.add(gson.fromJson(info, CommandInfo::class.java)) }
		}
	return list
}

fun findCommand(commandName: String) = commandInfo!!.first { info -> info.name == commandName }

val Command.syntax get() = "$serverPrefix${findCommand(name).syntax}"

val Command.example get() = "$serverPrefix${findCommand(name).example}"

val Command.category get() = findCommand(name).category

val Command.description get() = findCommand(name).description