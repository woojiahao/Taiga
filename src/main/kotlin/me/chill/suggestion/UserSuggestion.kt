package me.chill.suggestion

import org.joda.time.DateTime

data class UserSuggestion(
	val serverId: String,
	val suggesterId: String,
	val suggestionDescription: String,
	val suggestionDate: DateTime,
	val messageId: String? = null
)