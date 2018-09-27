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

	fun getChannel(targetChannel: TargetChannel, serverId: String) =
		getPreference(serverId, getCol(this@TargetChannel)) as String
}

fun getChannel(targetChannel: TargetChannel, serverId: String) =
	getPreference(serverId, selectColumn(targetChannel)) as String


private fun selectColumn(targetChannel: TargetChannel) =
	when (targetChannel) {
		TargetChannel.Logging -> Preference.loggingChannel
		TargetChannel.Join -> Preference.joinChannel
		TargetChannel.Suggestion -> Preference.suggestionChannel
	}
