package me.chill.credential

import me.chill.exception.TaigaException
import me.chill.json.configuration.Configuration
import me.chill.json.configuration.isHerokuRunning

class Credentials(configuration: Configuration?) {
	var token: String? = ""
	var database: String? = ""
	var defaultPrefix: String? = ""
	var botOwnerId: String = ""

	init {
		if (isHerokuRunning()) {
			token = System.getenv("BOT_TOKEN")
			database = System.getenv("JDBC_DATABASE_URL")
			defaultPrefix = System.getenv("PREFIX")

			if (token == null) throw TaigaException("Set a token environment variable in Heroku to proceed")
			if (database == null) throw TaigaException("Set up Heroku Postgres to proceed")
			if (defaultPrefix == null) throw TaigaException("Set a default prefix environment variable in Heroku to proceed")
		} else {
			if (configuration == null) throw TaigaException("Configuration cannot be null for a local instance of Taiga")

			token = configuration.token
			database = configuration.database
			defaultPrefix = configuration.prefix

			if (token == "enter your token") throw TaigaException("Set a token in the config.json to proceed")
			if (database == "enter your database url") throw TaigaException("Set a database url in the config.json to proceed")
			if (defaultPrefix == "enter your prefix") throw TaigaException("Set up a prefix in the config.json to proceed")
		}
	}
}
