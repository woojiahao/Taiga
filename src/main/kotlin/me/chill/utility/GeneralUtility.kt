package me.chill.utility

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun getDateTime() = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(DateTime.now())!!
fun Any.str() = this as String
fun Any.int() = this.str().toInt()
