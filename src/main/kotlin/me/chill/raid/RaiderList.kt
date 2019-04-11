package me.chill.raid

class RaiderList(private val duration: Int) {
  private val raiders = mutableListOf<Raider>()

  fun getRaider(userId: String) = raiders.first { it.userId == userId }

  fun hasRaider(userId: String) = raiders.any { it.userId == userId }

  fun addRaider(userId: String) {
    with (Raider(userId, 1)) {
      raiders += this
      startCountdown(duration) {
        raiders.remove(this)
      }
    }
  }
}