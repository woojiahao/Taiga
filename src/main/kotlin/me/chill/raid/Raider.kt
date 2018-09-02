package me.chill.raid

import me.chill.database.states.TimeMultiplier
import java.util.*
import kotlin.concurrent.timerTask

data class Raider(val userId: String, var messageCount: Int) {
	fun startCountdown(duration: Int, action: () -> Unit) {
		Timer().schedule(
			timerTask { action() },
			duration * TimeMultiplier.S.multiplier
		)
	}
}