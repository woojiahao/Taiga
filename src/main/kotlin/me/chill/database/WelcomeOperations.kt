package me.chill.database

fun getWelcomeMessage(serverId: String) = getPreference(serverId, Preference.welcomeMessage) as String

fun editWelcomeMessage(serverId: String, welcomeMessage: String) =
	updatePreferences(serverId) { it[this.welcomeMessage] = welcomeMessage }

fun getWelcomeDisabled(serverId: String) = getPreference(serverId, Preference.disableWelcome) as Boolean

fun disableWelcome(serverId: String) = editWelcomeDisabled(serverId, true)
fun enableWelcome(serverId: String) = editWelcomeDisabled(serverId, false)

fun editWelcomeDisabled(serverId: String, welcomeDisabled: Boolean) =
	updatePreferences(serverId) { it[this.disableWelcome] = welcomeDisabled }