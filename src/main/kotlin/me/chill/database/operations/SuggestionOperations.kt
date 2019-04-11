package me.chill.database.operations

import me.chill.database.Suggestion
import me.chill.suggestion.UserSuggestion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun getLatestSuggestionInPool(serverId: String) =
  transaction {
    val latestSuggestion = Suggestion
      .select { (Suggestion.serverId eq serverId) and Suggestion.messageId.isNull() }
      .orderBy(Suggestion.suggestionDate, SortOrder.DESC)
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

fun clearSuggestion(serverId: String, messageId: String) {
  transaction {
    Suggestion.deleteWhere { (Suggestion.serverId eq serverId) and (Suggestion.messageId eq messageId) }
  }
}

fun hasSuggestion(serverId: String, suggestionMessageId: String) =
  transaction {
    Suggestion.select {
      (Suggestion.serverId eq serverId) and (Suggestion.messageId eq suggestionMessageId)
    }.count() > 0
  }

private fun getLatestSuggestionId(serverId: String) =
  transaction {
    Suggestion
      .select { (Suggestion.serverId eq serverId) and Suggestion.messageId.isNull() }
      .orderBy(Suggestion.suggestionDate, false)
      .first()[Suggestion.suggestionId]
  }