package me.chill.infraction

class UserInfractionRecord(userId: String) {
	private val strikes = mutableListOf<UserStrike>()

	fun addStrike(strike: UserStrike) = strikes.add(strike)

	fun removeStrike(strike: UserStrike) = strikes.remove(strike)

	fun getStrikes() = strikes
}