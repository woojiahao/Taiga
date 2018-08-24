package me.chill

import me.chill.configurations.loadConfigurations
import me.chill.credentials.Credentials
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus

fun main(args: Array<String>) {
	val configurations = loadConfigurations() ?: return
	val credentials = Credentials(configurations)
	val jda: JDA = JDABuilder(AccountType.BOT)
		.setStatus(OnlineStatus.ONLINE)
		.setToken(credentials.token)
		.build()
}

