package me.chill.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun setupDatabase(databaseUrl: String) {
	Database.connect(databaseUrl, "org.postgresql.Driver")

	transaction {
		SchemaUtils.create(
			Permission,
			Preference,
			UserRecord,
			Strike,
			Raider,
			Suggestion,
			Macro,
			UserInvite
		)
	}
}

object Permission : Table() {
	val commandName = varchar("command_name", 50).primaryKey()
	val serverId = varchar("server_id", 20).primaryKey()
	val permission = varchar("permission", 20).primaryKey()
}

object Preference : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val prefix = varchar("prefix", 3)
	val joinChannel = varchar("join", 20)
	val loggingChannel = varchar("logging", 20)
	val suggestionChannel = varchar("suggestion", 20)
	val raidMessageLimit = integer("raid_message_limit")
	val raidMessageDuration = integer("raid_message_duration")
	val raidRoleExcluded = varchar("raid_role_excluded", 20).nullable()
	val disableWelcome = bool("disable_welcome")
	val welcomeMessage = text("welcome_message")
	val timeMultiplier = varchar("time_multiplier", 1)
	val onJoinRole = varchar("on_join_role", 20).nullable()
}

object UserRecord : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val userId = varchar("user_id", 20).primaryKey()
	val strikeId = integer("strike_id").primaryKey().references(Strike.strikeId)
}

object Strike : Table() {
	val strikeId = integer("strike_id").autoIncrement().primaryKey()
	val strikeWeight = integer("strike_weight")
	val strikeReason = text("strike_reason")
	val strikeDate = datetime("strike_date")
	val actingModeratorId = varchar("acting_moderator_id", 20)
	val expiryDate = datetime("expiry_date")
}

object Raider : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val userId = varchar("user_id", 20).primaryKey()
}

object Suggestion : Table() {
	val suggestionId = integer("suggestion_id").primaryKey().autoIncrement()
	val serverId = varchar("server_id", 20)
	val suggesterId = varchar("suggester_id", 20)
	val suggestionDescription = text("suggestion_description")
	val suggestionDate = datetime("suggestion_date")
	val messageId = varchar("message_id", 20).nullable()
}

object Macro : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val macroName = text("macro_name").primaryKey()
	val macroDescription = text("macro_description")
}

object UserInvite: Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val userId = varchar("user_id", 20).primaryKey()
	val invitesSent = integer("invites_sent")
}