package me.chill.database.preference

import me.chill.credentials
import me.chill.database.Preference
import me.chill.database.TimeMultiplier
import org.apache.commons.lang3.text.WordUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction

fun addServerPreference(serverId: String, defaultChannelId: String) {
	transaction {
		Preference.insert {
			it[this.serverId] = serverId
			it[prefix] = credentials!!.defaultPrefix!!
			it[joinChannel] = defaultChannelId
			it[loggingChannel] = defaultChannelId
			it[suggestionChannel] = defaultChannelId
			it[raidMessageLimit] = 3
			it[raidMessageDuration] = 5
			it[raidRoleExcluded] = null
			it[disableWelcome] = false
			it[welcomeMessage] = "Read #rules-and-info"
			it[timeMultiplier] = TimeMultiplier.M.name
			it[onJoinRole] = null
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
		Preference.columns.joinToString("\n") { "**${WordUtils.capitalize(it.name.replace("_", " "))}**\n${result[it]}\n" }
	}