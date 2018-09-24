package me.chill.utility

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.URL

fun getDateTime() = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(DateTime.now())!!
fun Any.str() = this as String
fun Any.int() = this.str().toInt()
fun String.readAPI() = Gson().fromJson(URL(this).readText(), JsonObject::class.java)!!
