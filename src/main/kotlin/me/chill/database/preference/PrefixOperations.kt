package me.chill.database.preference

import me.chill.database.Preference

fun getPrefix(serverId: String) = getPreference(serverId, Preference.prefix) as String

fun editPrefix(serverId: String, prefix: String) = updatePreferences(serverId) { it[this.prefix] = prefix }

