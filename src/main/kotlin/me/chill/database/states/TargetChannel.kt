package me.chill.database.states

import me.chill.database.Preference
import me.chill.database.operations.getPreference
import me.chill.database.operations.updatePreferences

enum class TargetChannel {
  Logging, Suggestion, Join, Useractivity;

  private fun getCol(targetChannel: TargetChannel) =
    when (targetChannel) {
      Logging -> Preference.loggingChannel
      Join -> Preference.joinChannel
      Suggestion -> Preference.suggestionChannel
      Useractivity -> Preference.userActivityChannel
    }

  private fun getDisabledCol(targetChannel: TargetChannel) =
    when (targetChannel) {
      Logging -> Preference.disableLogging
      Join -> Preference.disableWelcome
      Suggestion -> Preference.disableSuggestion
      Useractivity -> Preference.disableUserActivityTracking
    }

  private fun editDisabled(serverId: String, status: Boolean) =
    updatePreferences(serverId) { it[getDisabledCol(this@TargetChannel)] = status }

  fun edit(serverId: String, channelId: String) =
    updatePreferences(serverId) { it[getCol(this@TargetChannel)] = channelId }

  fun get(serverId: String) = getPreference(serverId, getCol(this@TargetChannel)) as String

  fun isDisabled(serverId: String) = getPreference(serverId, getDisabledCol(this)) as Boolean

  fun disableStatus(serverId: String) =
    when (isDisabled(serverId)) {
      true -> "disabled"
      false -> "enabled"
    }

  fun enable(serverId: String) = editDisabled(serverId, false)

  fun disable(serverId: String) = editDisabled(serverId, true)

  companion object {
    fun getNames() = TargetChannel.values().map { it.name.toLowerCase() }.toTypedArray()

    fun disableStatus(status: Boolean) =
      when (status) {
        true -> "disabled"
        false -> "enabled"
      }
  }
}