package me.chill.database.operations

import me.chill.database.Preference
import me.chill.database.states.TimeMultiplier

fun getTimeMultiplier(serverId: String) = TimeMultiplier.valueOf(getPreference(serverId, Preference.timeMultiplier) as String)

fun editTimeMultiplier(serverId: String, timeMultiplier: TimeMultiplier) =
  updatePreferences(serverId) { it[Preference.timeMultiplier] = timeMultiplier.name }