package me.chill

import me.chill.configurations.loadConfigurations
import me.chill.credentials.Credentials

fun main(args: Array<String>) {
	val configurations = loadConfigurations() ?: return
	val credentials = Credentials(configurations)
//	val jda: JDA = JDABuilder(AccountType.BOT)
//		.setStatus(OnlineStatus.ONLINE)
//		.setToken(credentials.token)
//		.build()
}

