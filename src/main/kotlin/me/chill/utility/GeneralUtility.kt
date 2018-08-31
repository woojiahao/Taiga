package me.chill.utility

import java.text.SimpleDateFormat
import java.util.*

fun getDateTime() = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().time)!!
fun Any.int() = (this as String).toInt()