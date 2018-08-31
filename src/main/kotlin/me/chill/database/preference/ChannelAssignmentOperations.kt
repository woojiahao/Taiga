package me.chill.database.preference

import me.chill.database.Preference
import me.chill.database.TargetChannel

fun editChannel(targetChannel: TargetChannel, serverId: String, channelId: String) =
	updatePreferences(serverId) { it[selectColumn(targetChannel)] = channelId }

fun getChannel(targetChannel: TargetChannel, serverId: String) =
	getPreference(serverId, selectColumn(targetChannel)) as String


private fun selectColumn(targetChannel: TargetChannel) =
	when (targetChannel) {
		TargetChannel.Logging -> Preference.loggingChannel
		TargetChannel.Join -> Preference.joinChannel
		TargetChannel.Suggestion -> Preference.suggestionChannel
	}
