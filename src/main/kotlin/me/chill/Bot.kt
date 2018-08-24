package me.chill

import me.chill.commands.events.OnJoinEvent
import me.chill.commands.events.OnLeaveEvent
import me.chill.configuration.isHerokuRunning
import me.chill.configuration.loadConfigurations
import me.chill.credential.Credentials
import me.chill.database.setupDatabase
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus

fun main(args: Array<String>) {
	val credentials = if (isHerokuRunning()) {
		Credentials(null)
	} else {
		val configurations = loadConfigurations() ?: return
		Credentials(configurations)
	}

	setupDatabase(credentials.database!!)

	val jda: JDA = JDABuilder(AccountType.BOT)
		.setStatus(OnlineStatus.ONLINE)
		.setToken(credentials.token)
		.addEventListener(OnJoinEvent(), OnLeaveEvent())
		.build()
}