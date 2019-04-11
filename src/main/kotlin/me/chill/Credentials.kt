package me.chill

import me.chill.exception.CredentialException

fun env(name: String, default: String = "", err: () -> Unit = { }): String {
  val env = System.getenv(name)
  return if (env == null) {
    err()
    default
  } else env
}

val token = env("BOT_TOKEN") {
  throw CredentialException(
    "Token",
    "Set token environment variable for Heroku to proceed"
  )
}

val databaseUrl = env("JDBC_DATABASE_URL") {
  throw CredentialException(
    "Database",
    "Set a default prefix environment variable in Heroku to proceed"
  )
}

val defaultPrefix = env("PREFIX") {
  throw CredentialException(
    "Default Prefix",
    "Set a default prefix environment variable in Heroku to proceed"
  )
}

val lyricsApiKey = env("LYRICS_API") {
  println("NOTE :: Song commands will not work without the Lyrics API key")
}

val songNameApiKey = env("SONG_NAME_API") {
  println("NOTE :: Song commands will not work without the Song Name API key")
}

var botOwnerId = ""