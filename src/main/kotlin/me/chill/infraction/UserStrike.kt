package me.chill.infraction

import org.joda.time.DateTime

data class UserStrike(val strikeId: Int,
					  val strikeWeight: Int,
					  val strikeReason: String,
					  val strikeDate: DateTime,
					  val actingModeratorId: String,
					  val expiryDate: DateTime)