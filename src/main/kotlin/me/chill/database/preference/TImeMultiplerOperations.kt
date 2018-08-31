package me.chill.database.preference

import me.chill.database.Preference
import me.chill.database.TimeMultiplier

fun getTimeMultiplier(serverId: String) = getPreference(serverId, Preference.timeMultiplier) as TimeMultiplier

fun editTimeMultiplier(serverId: String, timeMultiplier: TimeMultiplier) =
	updatePreferences(serverId) { it[this.timeMultiplier] = timeMultiplier.name }