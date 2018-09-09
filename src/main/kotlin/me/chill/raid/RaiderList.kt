package me.chill.raid

class RaiderList(private val duration: Int) {
	private val raiders = mutableListOf<Raider>()

	fun getRaider(userId: String) = raiders.first { it.userId == userId }

	fun hasRaider(userId: String) = raiders.any { it.userId == userId }

	fun addRaider(userId: String) {
		val raider = Raider(userId, 1)
		raiders.add(raider)
		raider.startCountdown(duration) { raiders.remove(raider) }
	}
}