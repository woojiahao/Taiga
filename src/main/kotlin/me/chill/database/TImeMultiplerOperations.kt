package me.chill.database

import me.chill.database.states.TimeMultiplier

fun getTimeMultiplier(serverId: String) = TimeMultiplier.valueOf(getPreference(serverId, Preference.timeMultiplier) as String)

fun editTimeMultiplier(serverId: String, timeMultiplier: TimeMultiplier) =
	updatePreferences(serverId) { it[this.timeMultiplier] = timeMultiplier.name }