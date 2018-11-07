package me.chill.database.operations

import me.chill.database.Preference

fun getWelcomeMessage(serverId: String) = getPreference(serverId, Preference.welcomeMessage) as String

fun editWelcomeMessage(serverId: String, welcomeMessage: String) =
  updatePreferences(serverId) { it[Preference.welcomeMessage] = welcomeMessage }
