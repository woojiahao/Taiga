package me.chill.database.operations

import me.chill.credentials
import me.chill.database.Preference
import me.chill.database.states.TimeMultiplier
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction

data class ServerPreference (
	val serverId: String,
	val prefix: String,
	val joinChannel: String,
	val loggingChannel: String,
	val suggestionChannel: String,
	val messageLimit: Int,
	val messageDuration: Int,
	val roleExcluded: String?,
	val welcomeDisabled: Boolean,
	val welcomeMessage: String,
	val timeMultiplier: TimeMultiplier,
	val onJoinRole: String?,
	val loggingDisabled: Boolean,
	val suggestionDisabled: Boolean
)

fun addServerPreference(serverId: String, defaultChannelId: String) {
	transaction {
		Preference.insert {
			it[Preference.serverId] = serverId
			it[prefix] = credentials!!.defaultPrefix!!
			it[joinChannel] = defaultChannelId
			it[loggingChannel] = defaultChannelId
			it[suggestionChannel] = defaultChannelId
			it[raidMessageLimit] = 5
			it[raidMessageDuration] = 3
			it[raidRoleExcluded] = null
			it[welcomeMessage] = "Welcome! Remember to read #rules-and-info"
			it[timeMultiplier] = TimeMultiplier.M.name
			it[onJoinRole] = null
			it[disableWelcome] = true
			it[disableLogging] = true
			it[disableSuggestion] = true
		}
	}
}

fun removeServerPreference(serverId: String) {
	transaction {
		Preference.deleteWhere { Preference.serverId eq serverId }
	}
}

fun getPreference(serverId: String, column: Column<*>) =
	transaction {
		Preference.select { Preference.serverId eq serverId }.first()[column]
	}

fun updatePreferences(serverId: String, updateStatement: Preference.(UpdateStatement) -> Unit) {
	transaction {
		Preference.update({ Preference.serverId eq serverId }, body = updateStatement)
	}
}

fun getAllPreferences(serverId: String) =
	transaction {
		val result = Preference.select { Preference.serverId eq serverId }.first()
		ServerPreference(
			result[Preference.serverId],
			result[Preference.prefix],
			result[Preference.joinChannel],
			result[Preference.loggingChannel],
			result[Preference.suggestionChannel],
			result[Preference.raidMessageLimit],
			result[Preference.raidMessageDuration],
			result[Preference.raidRoleExcluded],
			result[Preference.disableWelcome],
			result[Preference.welcomeMessage],
			TimeMultiplier.valueOf(result[Preference.timeMultiplier]),
			result[Preference.onJoinRole],
			result[Preference.disableLogging],
			result[Preference.disableSuggestion]
		)
	}