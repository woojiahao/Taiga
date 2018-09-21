package me.chill.database.operations

import me.chill.database.InviteWhitelist
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun addToWhitelist(serverId: String, inviteLink: String) =
	transaction {
		InviteWhitelist.insert {
			it[InviteWhitelist.serverId] = serverId
			it[InviteWhitelist.inviteLink] = inviteLink
		}
	}

fun removeFromWhitelist(serverId: String, inviteLink: String) =
	transaction {
		InviteWhitelist.deleteWhere { (InviteWhitelist.inviteLink eq inviteLink) and (InviteWhitelist.serverId eq serverId) }
	}