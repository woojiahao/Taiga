package me.chill

import me.chill.commands.events.InputEvent
import me.chill.commands.events.OnJoinEvent
import me.chill.commands.events.OnLeaveEvent
import me.chill.commands.framework.CommandContainer
import me.chill.credential.Credentials
import me.chill.database.setupDatabase
import me.chill.json.configuration.isHerokuRunning
import me.chill.json.configuration.loadConfigurations
import me.chill.json.help.CommandInfo
import me.chill.json.help.loadHelp
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game

// todo: add a command to dm all server owners if there is a problem detected
var credentials: Credentials? = null
var commandInfo: List<CommandInfo>? = null
fun main(args: Array<String>) {
	setup()

	JDABuilder(AccountType.BOT)
		.setStatus(OnlineStatus.ONLINE)
		.setToken(credentials!!.token)
		.setGame(Game.playing("${credentials!!.prefix}help"))
		.addEventListener(OnJoinEvent(), OnLeaveEvent(), InputEvent())
		.build()
}

fun setup() {
	credentials = if (isHerokuRunning()) {
		Credentials(null)
	} else {
		val configurations = loadConfigurations() ?: return
		Credentials(configurations)
	}

	setupDatabase(credentials!!.database!!)

	CommandContainer.loadContainer()
	commandInfo = loadHelp()
}