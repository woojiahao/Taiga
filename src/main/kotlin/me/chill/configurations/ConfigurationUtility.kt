package me.chill.configurations

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

private const val configDir = "config"
private const val configFile = "config.json"
private val gson = GsonBuilder().create()

private const val configPath = "${configDir}/${configFile}"

fun isHerokuRunning() = System.getenv("BOT_TOKEN") != null

fun loadConfigurations(): Configuration? {
	val configFile = File(configPath)
	return if (!configFile.exists()) {
		generateConfigurationFile(configFile)
		null
	} else {
		readConfigurationFile(configFile)
	}
}

private fun generateConfigurationFile(configFile: File) {
	println("Generating config.json\nPlease fill in the config.json and re-run the bot")

	configFile.parentFile.mkdir()
	configFile.createNewFile()

	val configuration = Configuration()
	val fileWriter = FileWriter(configPath)
	gson.toJson(configuration, fileWriter)
	fileWriter.close()
}

private fun readConfigurationFile(configFile: File): Configuration {
	println("Using existing config.json")

	val reader = JsonReader(FileReader(configFile))
	return gson.fromJson(reader, Configuration::class.java)
}
