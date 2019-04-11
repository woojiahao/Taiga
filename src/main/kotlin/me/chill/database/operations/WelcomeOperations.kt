package me.chill.database.operations

import me.chill.database.Preference

fun getWelcomeMessage(serverId: String) = getPreference<String>(serverId, Preference.welcomeMessage)

fun editWelcomeMessage(serverId: String, welcomeMessage: String) =
  updatePreferences(serverId) { it[Preference.welcomeMessage] = welcomeMessage }
