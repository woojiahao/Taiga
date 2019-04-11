package me.chill.database.operations

import me.chill.database.Preference

fun getPrefix(serverId: String) = getPreference<String>(serverId, Preference.prefix)

fun editPrefix(serverId: String, prefix: String) = updatePreferences(serverId) { it[Preference.prefix] = prefix }

