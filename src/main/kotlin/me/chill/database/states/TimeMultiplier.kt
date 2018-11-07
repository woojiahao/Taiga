package me.chill.database.states

enum class TimeMultiplier(val fullTerm: String, val multiplier: Long) {
  S("second", 1000L),
  M("minute", 60000L),
  H("hour", 3600000L),
  D("day", 86400000L);

  companion object {
    fun getNames(): Array<String> {
      val names = TimeMultiplier
        .values()
        .map { it.name.toLowerCase() }
        .toMutableList()
      names.addAll(TimeMultiplier.values().map { it.fullTerm })
      return names.toTypedArray()
    }
  }
}
