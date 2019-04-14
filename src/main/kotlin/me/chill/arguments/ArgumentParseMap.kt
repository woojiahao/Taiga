package me.chill.arguments

open class ArgumentParseMap(val message: String)

fun createParseMap(predicate: Boolean, successMsg: String, errorMessage: String) =
  if (predicate) SuccessParseMap(successMsg)
  else ErrorParseMap(errorMessage)