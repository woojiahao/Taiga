package me.chill.database

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun hasOnJoinRole(serverId: String) =
	transaction {
		!OnJoinRole.select { OnJoinRole.serverId eq serverId }.empty()
	}

fun getOnJoinRole(serverId: String) =
	transaction {
		OnJoinRole.select { OnJoinRole.serverId eq serverId }.first()[OnJoinRole.roleId]
	}

fun setOnJoinRole(serverId: String, roleId: String) {
	transaction {
		OnJoinRole.insert {
			it[OnJoinRole.serverId] = serverId
			it[OnJoinRole.roleId] = roleId
		}
	}
}

fun editOnJoinRole(serverId: String, roleId: String) {
	transaction {
		OnJoinRole.update({ OnJoinRole.serverId eq serverId }) { it[OnJoinRole.roleId] = roleId }
	}
}

fun removeOnJoinRole(serverId: String) {
	transaction {
		OnJoinRole.deleteWhere { OnJoinRole.serverId eq serverId }
	}
}