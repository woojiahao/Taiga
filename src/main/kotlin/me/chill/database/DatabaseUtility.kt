package me.chill.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun setupDatabase(databaseUrl: String) {
	Database.connect(databaseUrl, "org.postgresql.Driver")

	transaction {
		SchemaUtils.create(ChannelAssignment)
	}
}

object ChannelAssignment : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val loggingChannelId = varchar("logging", 20)
	val suggestionChannelId = varchar("suggestion", 20)
	val joinChannelId = varchar("join", 20)
}