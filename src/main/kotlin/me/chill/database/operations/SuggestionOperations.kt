package me.chill.database.operations

import me.chill.database.Suggestion
import me.chill.suggestion.UserSuggestion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun getPool(serverId: String): List<UserSuggestion> {
	val pool = mutableListOf<UserSuggestion>()
	transaction {
		Suggestion
			.select { (Suggestion.serverId eq serverId) and Suggestion.messageId.isNull() }
			.forEach {
				pool.add(
					UserSuggestion(
						it[Suggestion.serverId],
						it[Suggestion.suggesterId],
						it[Suggestion.suggestionDescription],
						it[Suggestion.suggestionDate]
					)
				)
			}
	}
	return pool
}

fun getLatestSuggestionInPool(serverId: String) =
	transaction {
		val latestSuggestion = Suggestion
			.select { (Suggestion.serverId eq serverId) }
			.orderBy(Suggestion.suggestionDate, false)
			.first()
		UserSuggestion(
			serverId,
			latestSuggestion[Suggestion.suggesterId],
			latestSuggestion[Suggestion.suggestionDescription],
			latestSuggestion[Suggestion.suggestionDate]
		)
	}


fun getPoolSize(serverId: String) =
	transaction {
		Suggestion.select { (Suggestion.serverId eq serverId) and Suggestion.messageId.isNull() }.count()
	}

fun addSuggestionToPool(serverId: String, suggesterId: String, suggestion: String) {
	transaction {
		Suggestion.insert {
			it[this.serverId] = serverId
			it[this.suggesterId] = suggesterId
			it[suggestionDescription] = suggestion
			it[suggestionDate] = DateTime.now()
			it[messageId] = null
		}
	}
}

fun denyLatestSuggestionInPool(serverId: String) {
	transaction {
		Suggestion.deleteWhere { Suggestion.suggestionId eq getLatestSuggestionId(serverId) }
	}
}

fun acceptLatestSuggestionInPool(serverId: String, suggestionMessageId: String) {
	transaction {
		val latestSuggestionId = getLatestSuggestionId(serverId)
		Suggestion.update({ Suggestion.suggestionId eq latestSuggestionId }) {
			it[messageId] = suggestionMessageId
		}
	}
}

private fun getLatestSuggestionId(serverId: String) =
	transaction {
		Suggestion
			.select { (Suggestion.serverId eq serverId) }
			.orderBy(Suggestion.suggestionDate, false)
			.first()[Suggestion.suggestionId]
	}