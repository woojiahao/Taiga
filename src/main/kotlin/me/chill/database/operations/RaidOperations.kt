package me.chill.database.operations

import me.chill.database.Raider
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

// todo: when history is invoked, show if the user is a raider in any other server
fun getRaiders(serverId: String) =
  transaction {
    Raider.select { Raider.serverId eq serverId }.map { it[Raider.userId] }
  }

fun addRaider(serverId: String, userId: String) {
  transaction {
    Raider.insert {
      it[Raider.serverId] = serverId
      it[Raider.userId] = userId
    }
  }
}

fun hasRaider(serverId: String, userId: String) =
  transaction {
    !Raider
      .select { raiderRecordMatch(serverId, userId) }
      .empty()
  }

fun freeRaider(serverId: String, userId: String) {
  transaction {
    Raider.deleteWhere { raiderRecordMatch(serverId, userId) }
  }
}

fun freeAll(serverId: String) {
  transaction {
    Raider.deleteWhere { Raider.serverId eq serverId }
  }
}

private fun raiderRecordMatch(serverId: String, userId: String) =
  (Raider.serverId eq serverId) and (Raider.userId eq userId)