package me.chill.utility.general

import java.text.SimpleDateFormat
import java.util.*

fun getDateTime() = SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().time)!!
