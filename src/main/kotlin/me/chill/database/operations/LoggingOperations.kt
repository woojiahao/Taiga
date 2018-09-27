package me.chill.database.operations

import me.chill.database.Preference

enum class LoggingType {
	Welcome, Suggestion, Logging;

	private fun getCol(type: LoggingType) =
		when (type) {
			Welcome -> Preference.disableWelcome
			Suggestion -> Preference.disableSuggestion
			Logging -> Preference.disableLogging
		}
	private fun editLoggingDisabled(serverId: String, status: Boolean) =
		updatePreferences(serverId) { it[getCol(this@LoggingType)] = status }

	fun isDisabled(serverId: String) = getPreference(serverId, getCol(this)) as Boolean
	fun enable(serverId: String) = editLoggingDisabled(serverId, false)
	fun disable(serverId: String) = editLoggingDisabled(serverId, true)
}

