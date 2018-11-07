package me.chill.database.operations

import me.chill.database.Preference

fun getRaidMessageLimit(serverId: String) = getPreference(serverId, Preference.raidMessageLimit) as Int

fun editRaidMessageLimit(serverId: String, messageLimit: Int) =
  updatePreferences(serverId) { it[raidMessageLimit] = messageLimit }

fun getRaidMessageDuration(serverId: String) = getPreference(serverId, Preference.raidMessageDuration) as Int

fun editRaidMessageDuration(serverId: String, messageDuration: Int) =
  updatePreferences(serverId) { it[raidMessageDuration] = messageDuration }

fun getRaidRoleExcluded(serverId: String) = getPreference(serverId, Preference.raidRoleExcluded) as String?

fun editRaidRoleExcluded(serverId: String, roleId: String) =
  updatePreferences(serverId) { it[raidRoleExcluded] = roleId }
