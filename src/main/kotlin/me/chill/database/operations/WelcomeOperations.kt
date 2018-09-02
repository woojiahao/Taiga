package me.chill.database.operations

import me.chill.database.Preference

fun getWelcomeMessage(serverId: String) = getPreference(serverId, Preference.welcomeMessage) as String

fun editWelcomeMessage(serverId: String, welcomeMessage: String) =
	updatePreferences(serverId) { it[Preference.welcomeMessage] = welcomeMessage }

fun getWelcomeDisabled(serverId: String) = getPreference(serverId, Preference.disableWelcome) as Boolean

fun disableWelcome(serverId: String) = editWelcomeDisabled(serverId, true)
fun enableWelcome(serverId: String) = editWelcomeDisabled(serverId, false)

fun editWelcomeDisabled(serverId: String, welcomeDisabled: Boolean) =
	updatePreferences(serverId) { it[disableWelcome] = welcomeDisabled }