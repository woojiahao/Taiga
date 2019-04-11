package me.chill.utility

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.read(text: String): T =
  fromJson<T>(text, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.read(element: JsonElement): T =
  fromJson<T>(element, object : TypeToken<T>() {}.type)