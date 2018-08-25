package me.chill.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun addPermission(commandName: String, serverId: String, roleId: String) {
	transaction {
		Permission.insert {
			it[Permission.commandName] = commandName
			it[Permission.serverId] = serverId
			it[Permission.permission] = roleId
		}
	}
}

fun removePermission(commandName: String, serverId: String) {
	transaction {
		Permission.deleteWhere { selectKey(serverId, commandName) }
	}
}

fun editPermission(commandName: String, serverId: String, roleId: String) {
	transaction {
		Permission.update({ selectKey(serverId, commandName) }) {
			it[Permission.permission] = roleId
		}
	}
}

fun hasPermission(commandName: String, serverId: String) =
	transaction {
		Permission.select { selectKey(serverId, commandName) }.any()
	}


fun viewPermissions(serverId: String): MutableMap<String, String> {
	val commandMap = mutableMapOf<String, String>()
	transaction {
		val results = Permission.select {
			Permission.serverId eq serverId
		}

		results.forEach { commandMap[it[Permission.commandName]] = it[Permission.permission] }
	}
	return commandMap
}

private fun selectKey(serverId: String, commandName: String) =
	(Permission.serverId eq serverId) and (Permission.commandName eq commandName)