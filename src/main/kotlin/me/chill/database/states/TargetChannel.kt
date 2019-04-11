package me.chill.database.states

import me.chill.database.Preference
import me.chill.database.operations.getPreference
import me.chill.database.operations.updatePreferences

enum class TargetChannel {
  LOGGING, SUGGESTION, JOIN, USER_ACTIVITY;

  private fun getCol(targetChannel: TargetChannel) =
    when (targetChannel) {
      LOGGING -> Preference.loggingChannel
      JOIN -> Preference.joinChannel
      SUGGESTION -> Preference.suggestionChannel
      USER_ACTIVITY -> Preference.userActivityChannel
    }

  private fun getDisabledCol(targetChannel: TargetChannel) =
    when (targetChannel) {
      LOGGING -> Preference.disableLogging
      JOIN -> Preference.disableWelcome
      SUGGESTION -> Preference.disableSuggestion
      USER_ACTIVITY -> Preference.disableUserActivityTracking
    }

  private fun editDisabled(serverId: String, status: Boolean) =
    updatePreferences(serverId) { it[getDisabledCol(this@TargetChannel)] = status }

  fun edit(serverId: String, channelId: String) =
    updatePreferences(serverId) { it[getCol(this@TargetChannel)] = channelId }

  fun get(serverId: String) = getPreference<String>(serverId, getCol(this@TargetChannel))

  fun isDisabled(serverId: String) = getPreference<Boolean>(serverId, getDisabledCol(this))

  fun disableStatus(serverId: String) = if (isDisabled(serverId)) "disabled" else "enabled"

  fun enable(serverId: String) = editDisabled(serverId, false)

  fun disable(serverId: String) = editDisabled(serverId, true)

  companion object {
    val names
      get() = TargetChannel.values().map { it.name.toLowerCase() }

    fun disableStatus(status: Boolean) = if (status) "disabled" else "enabled"
  }
}