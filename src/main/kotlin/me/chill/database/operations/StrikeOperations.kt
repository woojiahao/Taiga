package me.chill.database.operations

import me.chill.database.Strike
import me.chill.database.UserRecord
import me.chill.infraction.UserInfractionRecord
import me.chill.infraction.UserStrike
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun addStrike(serverId: String, targetId: String, strikeWeight: Int, strikeReason: String, actingModeratorId: String) {
	transaction {
		val currentDate = DateTime.now()
		val strikeId = Strike.insert {
			it[Strike.strikeWeight] = strikeWeight
			it[Strike.strikeReason] = strikeReason
			it[Strike.actingModeratorId] = actingModeratorId
			it[strikeDate] = currentDate
			it[expiryDate] = currentDate.plusMonths(1)
		}[Strike.strikeId]

		UserRecord.insert {
			it[UserRecord.serverId] = serverId
			it[userId] = targetId
			it[UserRecord.strikeId] = strikeId!!
		}
	}
}

fun getStrikeCount(serverId: String, userId: String) =
	transaction {
		UserRecord.join(
			Strike,
			JoinType.INNER,
			additionalConstraint = { Strike.strikeId eq UserRecord.strikeId }
		)
			.select {
				userRecordMatch(serverId, userId) and
					(Strike.expiryDate greater DateTime.now())
			}.map { it[Strike.strikeWeight] }.sum()
	}


fun getHistory(serverId: String, userId: String): UserInfractionRecord {
	val userRecord = UserInfractionRecord(userId)
	transaction {
		val strikeRecords = UserRecord
			.join(
				Strike,
				JoinType.INNER,
				additionalConstraint = { Strike.strikeId eq UserRecord.strikeId })
			.slice(
				Strike.strikeId,
				Strike.strikeWeight,
				Strike.strikeReason,
				Strike.strikeDate,
				Strike.actingModeratorId,
				Strike.expiryDate
			).select { userRecordMatch(serverId, userId) }

		strikeRecords.forEach {
			userRecord.addStrike(
				UserStrike(
					it[Strike.strikeId],
					it[Strike.strikeWeight],
					it[Strike.strikeReason],
					it[Strike.strikeDate],
					it[Strike.actingModeratorId],
					it[Strike.expiryDate]
				)
			)
		}
	}
	return userRecord
}

fun wipeRecord(serverId: String, userId: String) {
	transaction {
		val strikeIds = UserRecord
			.select { userRecordMatch(serverId, userId) }
			.map { it[UserRecord.strikeId] }
		UserRecord.deleteWhere { userRecordMatch(serverId, userId) }
		Strike.deleteWhere { Strike.strikeId inList strikeIds }
	}
}

private fun userRecordMatch(serverId: String, userId: String) = (UserRecord.userId eq userId) and (UserRecord.serverId eq serverId)