package me.chill.database.preference

import me.chill.database.Preference

fun getRaidMessageLimit(serverId: String) = getPreference(serverId, Preference.raidMessageLimit) as Int

fun editRaidMessageLimit(serverId: String, messageLimit: Int) =
	updatePreferences(serverId) { it[raidMessageLimit] = messageLimit }

fun getRaidMessageDuration(serverId: String) = getPreference(serverId, Preference.raidMessageDuration) as Int

fun editRaidMessageDuration(serverId: String, messageDuration: Int) =
	updatePreferences(serverId) { it[raidMessageDuration] = messageDuration }
