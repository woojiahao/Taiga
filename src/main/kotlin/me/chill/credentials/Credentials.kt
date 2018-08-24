package me.chill.credentials

import me.chill.configurations.Configuration

class Credentials(configuration: Configuration?) {
	val token = System.getenv("BOT_TOKEN") ?: configuration!!.token
	val database = System.getenv("JDBC_DATABASE_URL") ?: configuration!!.database
}
