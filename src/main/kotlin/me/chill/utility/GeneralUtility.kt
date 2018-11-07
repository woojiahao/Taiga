package me.chill.utility

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.HttpURLConnection
import java.net.URL

fun getDateTime() = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(DateTime.now())!!
fun Any.str() = this as String
fun Any.int() = this.str().toInt()
fun String.readAPI() = Gson().fromJson(URL(this).readText(), JsonObject::class.java)!!

inline fun <reified T : JsonElement> String.strictReadAPI(): Pair<Boolean, T?> {
  val url = URL(this)
  val conn = url.openConnection() as HttpURLConnection
  conn.requestMethod = "GET"
  conn.setRequestProperty("Content-Type", "application/json")
  conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")

  if (conn.responseCode >= 400) return Pair(false, null)

  val jsonResponse = conn.inputStream.bufferedReader().readLine()
  val data = Gson().fromJson<T>(jsonResponse, T::class.java)
  return Pair(true, data)
}