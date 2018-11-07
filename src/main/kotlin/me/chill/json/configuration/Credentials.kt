package me.chill.json.configuration

import me.chill.exception.CredentialException

class Credentials(configuration: Configuration?) {
  var token: String? = ""
  var database: String? = ""
  var defaultPrefix: String? = ""
  var lyricsApiKey: String? = ""
  var songNameApiKey: String? = ""

  var botOwnerId: String = ""

  init {
    if (isHerokuRunning()) {
      token = System.getenv("BOT_TOKEN")
      database = System.getenv("JDBC_DATABASE_URL")
      defaultPrefix = System.getenv("PREFIX")
      lyricsApiKey = System.getenv("LYRICS_API")
      songNameApiKey = System.getenv("SONG_NAME_API")

      token ?: throw CredentialException(
        "Token",
        "Set a token environment variable in Heroku to proceed"
      )
      database ?: throw CredentialException(
        "Database",
        "Set up Heroku Postgres to proceed"
      )
      defaultPrefix ?: throw CredentialException(
        "Default Prefix",
        "Set a default prefix environment variable in Heroku to proceed"
      )
      lyricsApiKey ?: println("NOTE :: Song commands will not work without the Lyrics API key")
      songNameApiKey ?: println("NOTE :: Song commands will not work without the Song Name API key")
    } else {
      configuration ?: throw CredentialException(
        "Configuration",
        "Configuration cannot be null for a local instance of Taiga"
      )

      token = configuration.token
      database = configuration.database
      defaultPrefix = configuration.prefix
      lyricsApiKey = configuration.lyricsApiKey
      songNameApiKey = configuration.songNameApiKey

      if (token == "enter your token")
        throw CredentialException("Token", "Set a token in the config.json to proceed")
      if (database == "enter your database url")
        throw CredentialException("Database", "Set a database url in the config.json to proceed")
      if (defaultPrefix == "enter your prefix")
        throw CredentialException("Default Prefix:", "Set up a prefix in the config.json to proceed")
      if (lyricsApiKey == "enter your lyrics API key") {
        println("NOTE :: Song commands will not work without the Lyrics API key")
        lyricsApiKey = null
      }
      if (songNameApiKey == "enter your song name API key") {
        println("NOTE :: Song commands will not work without the Song Name API key")
        songNameApiKey = null
      }
    }
  }
}
