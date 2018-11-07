package me.chill.database.operations

import me.chill.database.UserInvite
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun addUser(userId: String, serverId: String) {
  transaction {
    UserInvite.insert {
      it[UserInvite.userId] = userId
      it[UserInvite.serverId] = serverId
      it[invitesSent] = 1
    }
  }
}

fun hasUser(userId: String, serverId: String) =
  transaction {
    !UserInvite.select { userMatch(userId, serverId) }.empty()
  }

fun getUserCount(userId: String, serverId: String) =
  transaction {
    val count = UserInvite.select { userMatch(userId, serverId) }
    if (count.empty()) 0
    else count.first()[UserInvite.invitesSent]
  }

fun incrementInviteCount(userId: String, serverId: String) {
  transaction {
    val old = getUserCount(userId, serverId)
    UserInvite.update({ userMatch(userId, serverId) }) {
      it[invitesSent] = old + 1
    }
  }
}

fun removeUser(userId: String, serverId: String) {
  transaction {
    UserInvite.deleteWhere { userMatch(userId, serverId) }
  }
}

private fun userMatch(userId: String, serverId: String) = (UserInvite.serverId eq serverId) and (UserInvite.userId eq userId)