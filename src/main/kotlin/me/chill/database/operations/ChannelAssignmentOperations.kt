package me.chill.database.operations

import me.chill.database.Preference

enum class TargetChannel {
	Logging, Suggestion, Join;

	private fun getCol(targetChannel: TargetChannel) =
		when (targetChannel) {
			TargetChannel.Logging -> Preference.loggingChannel
			TargetChannel.Join -> Preference.joinChannel
			TargetChannel.Suggestion -> Preference.suggestionChannel
		}

	fun edit(serverId: String, channelId: String) =
		updatePreferences(serverId) { it[getCol(this@TargetChannel)] = channelId }

	fun get(serverId: String) =
		getPreference(serverId, getCol(this@TargetChannel)) as String
}