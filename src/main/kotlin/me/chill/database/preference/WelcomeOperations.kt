package me.chill.database.preference

import me.chill.database.Preference

fun getWelcomeMessage(serverId: String) = getPreference(serverId, Preference.welcomeMessage) as String

fun editWelcomeMessage(serverId: String, welcomeMessage: String) =
	updatePreferences(serverId) { it[this.welcomeMessage] = welcomeMessage }

fun getWelcomeDisabled(serverId: String) = getPreference(serverId, Preference.disableWelcome) as Boolean

fun editWelcomeDisabled(serverId: String, welcomeDisabled: Boolean) =
	updatePreferences(serverId) { it[this.disableWelcome] = welcomeDisabled }