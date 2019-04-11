package me.chill.database.operations

import me.chill.database.Preference

fun getRaidMessageLimit(serverId: String) = getPreference<Int>(serverId, Preference.raidMessageLimit)

fun editRaidMessageLimit(serverId: String, messageLimit: Int) =
  updatePreferences(serverId) { it[raidMessageLimit] = messageLimit }

fun getRaidMessageDuration(serverId: String) = getPreference<Int>(serverId, Preference.raidMessageDuration)

fun editRaidMessageDuration(serverId: String, messageDuration: Int) =
  updatePreferences(serverId) { it[raidMessageDuration] = messageDuration }

fun getRaidRoleExcluded(serverId: String) = getPreference<String?>(serverId, Preference.raidRoleExcluded)

fun editRaidRoleExcluded(serverId: String, roleId: String) =
  updatePreferences(serverId) { it[raidRoleExcluded] = roleId }
