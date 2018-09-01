package me.chill.database

import me.chill.infraction.UserInfractionRecord
import me.chill.infraction.UserStrike
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun strikeUser(serverId: String, targetId: String, strikeWeight: Int, strikeReason: String, actingModeratorId: String) {
	transaction {
		val currentDate = DateTime.now()
		val strikeId = Strike.insert {
			it[this.strikeWeight] = strikeWeight
			it[this.strikeReason] = strikeReason
			it[this.actingModeratorId] = actingModeratorId
			it[strikeDate] = currentDate
			it[expiryDate] = currentDate.plusMonths(1)
		}[Strike.strikeId]

		UserRecord.insert {
			it[this.serverId] = serverId
			it[userId] = targetId
			it[this.strikeId] = strikeId!!
		}
	}
}

fun getHistory(serverId: String, targetId: String): UserInfractionRecord {
	val userRecord = UserInfractionRecord(targetId)
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
			).select { (UserRecord.userId eq targetId) and (UserRecord.serverId eq serverId) }

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