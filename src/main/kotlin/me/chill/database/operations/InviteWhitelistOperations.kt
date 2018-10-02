package me.chill.database.operations

import me.chill.database.InviteWhitelist
import me.chill.database.Preference
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun addToWhitelist(serverId: String, inviteLink: String) =
	transaction {
		InviteWhitelist.insert {
			it[InviteWhitelist.serverId] = serverId
			it[InviteWhitelist.inviteLink] = inviteLink
		}
	}

fun hasInviteInWhitelist(serverId: String, inviteLink: String) =
	transaction {
		!InviteWhitelist.select { (InviteWhitelist.serverId eq serverId) and (InviteWhitelist.inviteLink eq inviteLink) }.empty()
	}

fun removeFromWhitelist(serverId: String, inviteLink: String) =
	transaction {
		InviteWhitelist.deleteWhere { (InviteWhitelist.inviteLink eq inviteLink) and (InviteWhitelist.serverId eq serverId) }
	}

fun getWhitelist(serverId: String) =
	transaction {
		InviteWhitelist.select { InviteWhitelist.serverId eq serverId }.joinToString("\n") {
			"- ${it[InviteWhitelist.inviteLink]}"
		}
	}

fun getInviteExcluded(serverId: String) = getPreference(serverId, Preference.inviteRoleExcluded) as String?

fun editInviteExcluded(serverId: String, roleId: String) = updatePreferences(serverId) { it[inviteRoleExcluded] = roleId }