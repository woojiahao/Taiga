package me.chill.database

enum class TimeMultiplier(val fullTerm: String, val multiplier: Long) {
	S("second", 1000L),
	M("minute", 60000L),
	H("hour", 3600000L),
	D("day", 86400000L);

}
