package me.chill.database.preference

import me.chill.database.Preference
import me.chill.database.TimeMultiplier

fun getTimeMultiplier(serverId: String) = TimeMultiplier.valueOf(getPreference(serverId, Preference.timeMultiplier) as String)

fun editTimeMultiplier(serverId: String, timeMultiplier: TimeMultiplier) =
	updatePreferences(serverId) { it[this.timeMultiplier] = timeMultiplier.name }