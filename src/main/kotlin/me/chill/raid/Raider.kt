package me.chill.raid

import me.chill.database.states.TimeMultiplier
import java.util.*
import kotlin.concurrent.timerTask

data class Raider(val userId: String, var messageCount: Int) {
  /**
   * Countdown that is applied to each user every time that they are added to the raid watch list
   * Timeout is applied in (S)econds
   */
  fun startCountdown(duration: Int, action: () -> Unit) {
    Timer().schedule(
      timerTask { action() },
      duration * TimeMultiplier.S.multiplier
    )
  }
}